package com.bn.ninjatrader.simulation.scanner;

import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.AlgorithmDao;
import com.bn.ninjatrader.model.entity.Algorithm;
import com.bn.ninjatrader.model.mongo.guice.NtModelMongoModule;
import com.bn.ninjatrader.simulation.core.SimulationFactory;
import com.bn.ninjatrader.simulation.core.SimulationRequest;
import com.bn.ninjatrader.simulation.exception.AlgorithmIdNotFoundException;
import com.bn.ninjatrader.simulation.guice.NtSimulationModule;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.bn.ninjatrader.simulation.algorithm.AlgorithmScript;
import com.bn.ninjatrader.simulation.algorithm.AlgorithmScriptFactory;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class StockScanner {
  private static final Logger LOG = LoggerFactory.getLogger(StockScanner.class);

  private final SimulationFactory simulationFactory;
  private final PriceDao priceDao;
  private final AlgorithmDao tradeAlgorithmDao;
  private final AlgorithmScriptFactory algorithmScriptFactory;
  private final Clock clock;

  @Inject
  public StockScanner(final SimulationFactory simulationFactory,
                      final PriceDao priceDao,
                      final AlgorithmDao tradeAlgorithmDao,
                      final AlgorithmScriptFactory algorithmScriptFactory,
                      final Clock clock) {
    this.simulationFactory = simulationFactory;
    this.priceDao = priceDao;
    this.tradeAlgorithmDao = tradeAlgorithmDao;
    this.algorithmScriptFactory = algorithmScriptFactory;
    this.clock = clock;
  }

  public List<ScanResult> scan(final ScanRequest req) {
    final LocalDate from = LocalDate.now(clock).minusYears(1);
    final LocalDate to = LocalDate.now(clock);
    final Set<String> symbols = priceDao.findAllSymbols();
    final Algorithm algorithm = tradeAlgorithmDao.findByAlgorithmId(req.getAlgoId())
        .orElseThrow(() -> new AlgorithmIdNotFoundException(req.getAlgoId()));
    final AlgorithmScript algoScript = algorithmScriptFactory.create(algorithm);

    // Collect reports for each symbol.
    final List<SimulationReport> reports =  symbols.stream()
        .map(symbol -> simulationFactory.create(SimulationRequest.withSymbol(symbol)
            .from(from).to(to).algorithmScript(algoScript)).play()
        ).filter(report -> {
          if (!report.getTransactions().isEmpty()) {
            final Transaction txn = report.getTransactions().get(report.getTransactions().size()-1);
            if (txn.getDate().plusDays(req.getDays()).isAfter(to)) {
              return true;
            }
          }
          return false;
        }).collect(Collectors.toList());

    // Scan each report and collect their last transaction
    final List<ScanResult> scanResults = reports.stream().map(report -> {
      final double profit = report.getEndingCash() - report.getStartingCash();
      final double profitPcnt = NumUtil.divide(profit, report.getStartingCash());
      return ScanResult.builder()
          .symbol(report.getSymbol())
          .profit(profit)
          .profitPcnt(profitPcnt)
          .lastTransaction(report.getTransactions().get(report.getTransactions().size() - 1))
          .build();
    })
        .sorted(comparing(scanResult -> scanResult.getSymbol()))
        .collect(Collectors.toList());

    return scanResults;
  }

  public static void main(String args[]) {
    System.setProperty("mongo.host", "192.168.99.100:32768");
    System.setProperty("mongo.database.name", "ninja_trader");

    Injector injector = Guice.createInjector(
        new NtModelMongoModule(),
        new NtSimulationModule()
        );

    List<ScanResult> results = injector.getInstance(StockScanner.class).scan(ScanRequest.withAlgoId("ADMIN").days(1));
  }
}

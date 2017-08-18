package com.bn.ninjatrader.simulation.scanner;

import com.bn.ninjatrader.common.model.Algorithm;
import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.model.dao.AlgorithmDao;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.simulation.algorithm.AlgorithmScript;
import com.bn.ninjatrader.simulation.algorithm.AlgorithmScriptFactory;
import com.bn.ninjatrader.simulation.core.SimulationFactory;
import com.bn.ninjatrader.simulation.core.SimulationRequest;
import com.bn.ninjatrader.simulation.exception.AlgorithmNotFoundException;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class DefaultStockScanner implements StockScanner {
  private static final Logger LOG = LoggerFactory.getLogger(DefaultStockScanner.class);

  private final SimulationFactory simulationFactory;
  private final PriceDao priceDao;
  private final AlgorithmDao tradeAlgorithmDao;
  private final AlgorithmScriptFactory algorithmScriptFactory;
  private final Clock clock;

  @Inject
  public DefaultStockScanner(final SimulationFactory simulationFactory,
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

  @Override
  public Map<String, ScanResult> scan(final ScanRequest req) {
    final LocalDate from = LocalDate.now(clock).minusYears(1);
    final LocalDate to = LocalDate.now(clock);

    // Get stock symbols to scan
    final Collection<String> symbols = prepareSymbols(req);

    // Prepare algorithm script to scan with
    final AlgorithmScript algoScript = prepareAlgorithmScript(req);

    // Play simulation and collect reports for each symbol
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
    final Map<String, ScanResult> scanResults = collectScanResults(reports);

    return scanResults;
  }

  /**
   * Either find all symbols or use given symbols from request.
   */
  private Collection<String> prepareSymbols(final ScanRequest req) {
    final Collection<String> symbols;
    if (req.getSymbols() == null || req.getSymbols().isEmpty()) {
      symbols = priceDao.findAllSymbols();
    } else {
      symbols = req.getSymbols();
    }
    return symbols;
  }

  /**
   * Either retrieve algorithm from database or use the given one from request.
   */
  private AlgorithmScript prepareAlgorithmScript(final ScanRequest req) {
    final Algorithm algorithm = req.getAlgorithm() == null ?
        findAlgorithmById(req.getAlgorithmId()) :
        req.getAlgorithm();
    final AlgorithmScript algoScript = algorithmScriptFactory.create(algorithm);
    return algoScript;
  }

  /**
   * Find algorithm from database with given algorithmId
   */
  private Algorithm findAlgorithmById(final String algorithmId) {
    return tradeAlgorithmDao.findOneByAlgorithmId(algorithmId)
        .orElseThrow(() -> new AlgorithmNotFoundException(algorithmId));
  }

  /**
   * Scan each SimulationReport and get latest transaction.
   * @param reports
   * @return ScanResult containing latest transaction
   */
  private Map<String, ScanResult> collectScanResults(final Collection<SimulationReport> reports) {
    return reports.stream().map(report -> {
      final double profit = report.getEndingCash() - report.getStartingCash(); //TODO ending cash not accurate. use total profits. you ddnt sell the last txn.
      final double profitPcnt = NumUtil.divide(profit, report.getStartingCash()); // TODO no need to calculate! include in sim report

      final Transaction lastTransaction = report.getTransactions().stream()
          .max(comparing(txn -> txn.getDate()))
          .get();

      return ScanResult.builder()
          .symbol(report.getSymbol())
          .profit(profit)
          .profitPcnt(profitPcnt)
          .lastTransaction(lastTransaction)
          .build();

    }).collect(Collectors.toMap(ScanResult::getSymbol, Function.identity()));
  }
}

package com.bn.ninjatrader.simulation.scanner;

import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.simulation.Simulator;
import com.bn.ninjatrader.simulation.core.SimulationRequest;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import com.google.inject.Inject;
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

  private final Simulator simulator;
  private final PriceDao priceDao;
  private final Clock clock;

  @Inject
  public StockScanner(final Simulator simulator,
                      final PriceDao priceDao,
                      final Clock clock) {
    this.simulator = simulator;
    this.priceDao = priceDao;
    this.clock = clock;
  }

  public List<ScanResult> scan(final ScanRequest req) {
    final LocalDate from = LocalDate.now(clock).minusYears(1);
    final LocalDate to = LocalDate.now(clock);
    final Set<String> symbols = priceDao.findAllSymbols();

    // Collect reports for each symbol.
    final List<SimulationReport> reports =  symbols.parallelStream()
        .map(symbol -> simulator.play(SimulationRequest.withSymbol(symbol)
            .from(from).to(to).tradeAlgorithmId(req.getAlgoId()))
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
          .symbol(report.getSimulationParams().getSymbol())
          .profit(profit)
          .profitPcnt(profitPcnt)
          .lastTransaction(report.getTransactions().get(report.getTransactions().size() - 1))
          .build();
    })
        .sorted(comparing(scanResult -> scanResult.getSymbol()))
        .collect(Collectors.toList());

    return scanResults;
  }
}

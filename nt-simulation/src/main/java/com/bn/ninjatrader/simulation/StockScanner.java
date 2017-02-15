package com.bn.ninjatrader.simulation;

import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.guice.NtModelModule;
import com.bn.ninjatrader.simulation.core.SimulationParams;
import com.bn.ninjatrader.simulation.guice.NtSimulationModule;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class StockScanner {
  private static final Logger LOG = LoggerFactory.getLogger(StockScanner.class);

  private final Simulator simulator;
  private final PriceDao priceDao;
  private final GoldenAlgo algo;

  @Inject
  public StockScanner(final Simulator simulator,
                      final PriceDao priceDao,
                      final GoldenAlgo algo) {
    this.simulator = simulator;
    this.priceDao = priceDao;
    this.algo = algo;
  }

  public void scan() {
    final LocalDate from = LocalDate.now().minusYears(1);
    final LocalDate to = LocalDate.now();
    final List<SimulationReport> actionReports = Lists.newArrayList();

    for (final String symbol : priceDao.findAllSymbols()) {
      final SimulationParams params = algo.from(from).to(to).forSymbol(symbol);
      final SimulationReport report = simulator.play(params);
      if (!report.getTransactions().isEmpty()) {
        final Transaction txn = report.getTransactions().get(report.getTransactions().size()-1);
        if (txn.getDate().plusDays(4).isAfter(to)) {
          actionReports.add(report);
        }
      }
    }

    for(final SimulationReport report : actionReports) {
      final String symbol = report.getSimulationParams().getSymbol();
      final Transaction txn = report.getTransactions().get(report.getTransactions().size()-1);
      LOG.info("{} - {} - {}", symbol, report.getTradeStatistic().getTotalProfit(), txn);
    }
  }

  public static void main(final String args[]) {
    final Injector injector = Guice.createInjector( new NtModelModule(), new NtSimulationModule());
    final StockScanner stockScanner = injector.getInstance(StockScanner.class);
    stockScanner.scan();
  }
}

package com.bn.ninjatrader.simulation.scanner;

import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import com.bn.ninjatrader.simulation.transaction.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class SimulationReportConverter implements Serializable {
  private static final Logger LOG = LoggerFactory.getLogger(SimulationReportConverter.class);

  public Map<String, ScanResult> convert(final Collection<SimulationReport> reports) {
    return reports.stream()
        .filter(report -> !report.getTransactions().isEmpty())
        .map(report -> {
          final double profit = report.getEndingCash() - report.getStartingCash(); //TODO ending cash not accurate. use total profits. you ddnt sell the last txn.
          final double profitPcnt = NumUtil.divide(profit, report.getStartingCash()); // TODO no need to calculate! include in sim report

          final Transaction lastTransaction = report.getTransactions().stream()
              .filter(txn -> txn.getTransactionType() != TransactionType.CLEANUP) // Ignore cleanup as this is system and not algorithm triggered.
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

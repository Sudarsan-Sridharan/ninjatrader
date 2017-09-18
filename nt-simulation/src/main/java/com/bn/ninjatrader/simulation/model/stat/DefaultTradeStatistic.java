package com.bn.ninjatrader.simulation.model.stat;

import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.simulation.transaction.SellTransaction;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Brad on 8/3/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DefaultTradeStatistic implements TradeStatistic {
  private static final Logger LOG = LoggerFactory.getLogger(DefaultTradeStatistic.class);

  @JsonIgnore
  private final List<Transaction> transactions;

  public DefaultTradeStatistic() {
    transactions = Lists.newArrayList();
  }

  @Override
  public int getNumOfTrades() {
    return (int) sellTransaction().count();
  }

  @Override
  public int getNumOfWins() {
    return (int) sellTransaction().filter(txn -> txn.getProfit() > 0).count();
  }

  @Override
  public int getNumOfLosses() {
    return (int) sellTransaction().filter(txn -> txn.getProfit() <= 0).count();
  }

  @Override
  public double getTotalGain() {
    final double total = sellTransaction()
        .filter(txn -> txn.getProfit() > 0)
        .collect(Collectors.summingDouble(txn -> txn.getProfit()));
    return NumUtil.trim(total, 2);
  }

  @Override
  public double getTotalLoss() {
    final double total = sellTransaction()
        .filter(txn -> txn.getProfit() < 0)
        .collect(Collectors.summingDouble(txn -> txn.getProfit()));
    return NumUtil.trim(total, 2);
  }

  @Override
  public double getTotalProfit() {
    final double total = sellTransaction()
        .collect(Collectors.summingDouble(txn -> txn.getProfit()));
    return NumUtil.trim(total, 2);
  }

  @Override
  public SellTransaction getMaxGainTxn() {
    return sellTransaction()
        .filter(txn -> txn.getProfit() > 0)
        .max(Comparator.comparing(txn -> txn.getProfit())).orElse(null);
  }

  @Override
  public SellTransaction getMaxLossTxn() {
    return sellTransaction()
        .filter(txn -> txn.getProfit() <= 0)
        .min(Comparator.comparing(txn -> txn.getProfit())).orElse(null);
  }

  @Override
  public SellTransaction getMaxPcntGainTxn() {
    return sellTransaction()
        .filter(txn -> txn.getProfitPcnt() > 0)
        .max(Comparator.comparing(txn -> txn.getProfitPcnt())).orElse(null);
  }

  @Override
  public SellTransaction getMaxPcntLossTxn() {
    return sellTransaction()
        .filter(txn -> txn.getProfitPcnt() <= 0)
        .min(Comparator.comparing(txn -> txn.getProfitPcnt())).orElse(null);
  }

  @Override
  public double getProfitPerTrade() {
    double profitPerTrade = getTotalProfit() / (double) getNumOfTrades();
    return NumUtil.trim(profitPerTrade, 2);
  }

  @Override
  public double getWinPcnt() {
    return (double) getNumOfWins() / getNumOfTrades();
  }

  @Override
  public double getLossPcnt() {
    return (double) getNumOfLosses() / getNumOfTrades();
  }

  @Override
  public void collectStats(final Transaction transaction) {
    if (transaction == null) {
      return;
    }
    transactions.add(transaction);
  }

  @Override
  public List<Transaction> getTransactions() {
    return transactions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DefaultTradeStatistic that = (DefaultTradeStatistic) o;
    return Objects.equal(transactions, that.transactions);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(transactions);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("transactions", transactions)
        .toString();
  }

  private Stream<SellTransaction> sellTransaction() {
    return transactions.stream()
        .filter(txn -> txn instanceof SellTransaction)
        .map(SellTransaction.class::cast);
  }
}

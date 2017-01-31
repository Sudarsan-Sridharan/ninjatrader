package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 8/12/16.
 */
public class Portfolio {

  private static final Logger LOG = LoggerFactory.getLogger(Portfolio.class);

  private List<BuyTransaction> buyTransactions = Lists.newArrayList();

  public void add(final BuyTransaction buyTransaction) {
    checkNotNull(buyTransaction, "buyTransaction must not be null.");
    buyTransactions.add(buyTransaction);
  }

  public long getTotalShares() {
    long totalShares = 0;
    for (final BuyTransaction transaction : buyTransactions) {
      totalShares += transaction.getNumOfShares();
    }
    return totalShares;
  }

  public double getAvgPrice() {
    double totalValue = 0;
    double totalShares = 0;
    for (final BuyTransaction transaction : buyTransactions) {
      totalValue += transaction.getValue();
      totalShares += transaction.getNumOfShares();
    }
    return NumUtil.divide(totalValue, totalShares);
  }

  public double getEquityValue() {
    double totalEquityValue = 0;
    for (final BuyTransaction transaction : buyTransactions) {
      totalEquityValue = NumUtil.plus(totalEquityValue, transaction.getValue());
    }
    return totalEquityValue;
  }

  public boolean isEmpty() {
    return buyTransactions.isEmpty();
  }

  public void clear() {
    buyTransactions.clear();
  }

  public List<BuyTransaction> getBuyTransactions() {
    return Lists.newArrayList(buyTransactions);
  }
}

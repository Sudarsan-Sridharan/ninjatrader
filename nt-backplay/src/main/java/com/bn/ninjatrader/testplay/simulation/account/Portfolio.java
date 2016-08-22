package com.bn.ninjatrader.testplay.simulation.account;

import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.testplay.simulation.transaction.BuyTransaction;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Brad on 8/12/16.
 */
public class Portfolio {

  private static final Logger log = LoggerFactory.getLogger(Portfolio.class);

  private List<BuyTransaction> buyTransactions = Lists.newArrayList();

  public void add(BuyTransaction buyTransaction) {
    buyTransactions.add(buyTransaction);
  }

  public long getTotalShares() {
    long totalShares = 0;
    for (BuyTransaction transaction : buyTransactions) {
      totalShares += transaction.getNumOfShares();
    }
    return totalShares;
  }

  public double getAvgPrice() {
    double totalValue = 0;
    double totalShares = 0;
    for (BuyTransaction transaction : buyTransactions) {
      totalValue += transaction.getValue();
      totalShares += transaction.getNumOfShares();
    }
    return NumUtil.divide(totalValue, totalShares);
  }

  public boolean isEmpty() {
    return buyTransactions.isEmpty();
  }

  public void clear() {
    buyTransactions.clear();
  }
}

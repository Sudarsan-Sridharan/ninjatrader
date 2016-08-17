package com.bn.ninjatrader.testplay.simulation.transaction;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by Brad on 8/3/16.
 */
public class Bookkeeper {

  private List<Transaction> tradeLogs = Lists.newArrayList();

  public void keep(Transaction log) {
    tradeLogs.add(log);
  }

  public int getNumOfTrades() {
    return tradeLogs.size();
  }

  public List<Transaction> getTransactions() {
    return tradeLogs;
  }
}

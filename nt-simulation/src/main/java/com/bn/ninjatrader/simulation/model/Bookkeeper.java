package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.simulation.transaction.Transaction;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Brad on 8/3/16.
 */
public class Bookkeeper {

  private static final Logger log = LoggerFactory.getLogger(Bookkeeper.class);

  private List<Transaction> transactions = Lists.newArrayList();

  public void keep(final Transaction log) {
    transactions.add(log);
  }

  public int getNumOfTrades() {
    return transactions.size();
  }

  public List<Transaction> getTransactions() {
    return transactions;
  }
}

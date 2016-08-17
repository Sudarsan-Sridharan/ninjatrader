package com.bn.ninjatrader.testplay.simulation.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Brad on 8/3/16.
 */
public class Account {

  private static final Logger log = LoggerFactory.getLogger(Account.class);

  private double cash = 100000;
  private double startingCash;
  private final Portfolio portfolio = new Portfolio();

  public static Account startWithCash(double cash) {
    Account account = new Account();
    account.cash = cash;
    account.startingCash = cash;
    return account;
  }

  private Account() {}

  public double getCash() {
    return cash;
  }

  public double getStartingCash() {
    return startingCash;
  }

  public void addCash(double amount) {
    cash += amount;
  }

  public Portfolio getPortfolio() {
    return portfolio;
  }
}

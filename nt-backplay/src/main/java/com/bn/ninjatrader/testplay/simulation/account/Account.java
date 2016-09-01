package com.bn.ninjatrader.testplay.simulation.account;

import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.testplay.simulation.stats.TradeStatistic;
import com.bn.ninjatrader.testplay.simulation.transaction.Bookkeeper;
import com.bn.ninjatrader.testplay.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.testplay.simulation.transaction.SellTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Brad on 8/3/16.
 */
public class Account {
  private static final Logger log = LoggerFactory.getLogger(Account.class);

  private final Portfolio portfolio = new Portfolio();
  private final Bookkeeper bookkeeper = new Bookkeeper();
  private final TradeStatistic tradeStatistic = new TradeStatistic();

  private double cash = 100000;
  private double startingCash = 100000;

  public static Account withStartingCash(double cash) {
    Account account = new Account();
    account.cash = cash;
    account.startingCash = cash;
    return account;
  }

  public void onBuySuccess(BuyTransaction transaction) {
    bookkeeper.keep(transaction);
  }

  public void onSellSuccess(SellTransaction transaction) {
    bookkeeper.keep(transaction);
    tradeStatistic.collectStats(transaction);
  }

  public double getCash() {
    return cash;
  }

  public void addCash(double amount) {
    cash += amount;
  }

  public double getProfit() {
    double profit = NumUtil.minus(cash, startingCash);
    return NumUtil.trim(profit, 2);
  }

  public long getNumOfShares() {
    return portfolio.getTotalShares();
  }

  public double getAvgPrice() {
    return portfolio.getAvgPrice();
  }

  public boolean hasShares() {
    return !portfolio.isEmpty();
  }

  public void clearPortfolio() {
    portfolio.clear();
  }

  public void addToPortfolio(BuyTransaction transaction) {
    portfolio.add(transaction);
  }

  public void setStartingCash(double startingCash) {
    this.startingCash = startingCash;
  }

  public void print() {
    double profit = getProfit();

    bookkeeper.print();
    tradeStatistic.print();

    log.info("Starting Cash: {}", startingCash);
    log.info("Ending Cash: {}", (long) cash);
    log.info("% Gain: {}%", NumUtil.toPercent(profit / startingCash));
  }
}

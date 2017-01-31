package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.simulation.transaction.SellTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Brad on 8/3/16.
 */
public class Account {
  private static final Logger LOG = LoggerFactory.getLogger(Account.class);

  private final Portfolio portfolio = new Portfolio();
  private final Bookkeeper bookkeeper = new Bookkeeper();
  private final TradeStatistic tradeStatistic = new TradeStatistic();

  private double liquidCash = 100000;
  private double startingCash = 100000;

  public static Account withStartingCash(final double cash) {
    final Account account = new Account();
    account.liquidCash = cash;
    account.startingCash = cash;
    return account;
  }

  public void onBuySuccess(final BuyTransaction transaction) {
    bookkeeper.keep(transaction);
  }

  public void onSellSuccess(final SellTransaction transaction) {
    bookkeeper.keep(transaction);
    tradeStatistic.collectStats(transaction);
  }

  public double getLiquidCash() {
    return liquidCash;
  }

  public void addCash(final double amount) {
    liquidCash += amount;
  }

  public double getProfit() {
    double profit = NumUtil.minus(liquidCash, startingCash);
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

  public void addToPortfolio(final BuyTransaction transaction) {
    portfolio.add(transaction);
  }

  public void setStartingCash(final double startingCash) {
    this.startingCash = startingCash;
  }

  public TradeStatistic getTradeStatistic() {
    return tradeStatistic;
  }

  public Bookkeeper getBookkeeper() {
    return bookkeeper;
  }

  public Portfolio getPortfolio() {
    return portfolio;
  }

  public void print() {
    final double profit = getProfit();

    bookkeeper.print();
    tradeStatistic.print();

    LOG.info("Starting Cash: {}", startingCash);
    LOG.info("Ending Cash: {}", (long) liquidCash);
    LOG.info("% Gain: {}%", NumUtil.toPercent(profit / startingCash));
  }
}

package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.listener.BrokerListener;
import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.simulation.transaction.SellTransaction;
import com.google.common.base.MoreObjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Brad on 8/3/16.
 */
public class Account implements BrokerListener {
  private static final Logger LOG = LoggerFactory.getLogger(Account.class);

  private final Portfolio portfolio;
  private final Bookkeeper bookkeeper;
  private final TradeStatistic tradeStatistic;

  private double liquidCash = 100000;
  private double startingCash = 100000;

  public Account(final Portfolio portfolio,
                 final Bookkeeper bookkeeper,
                 final TradeStatistic tradeStatistic,
                 final double startingCash) {
    this.portfolio = portfolio;
    this.bookkeeper = bookkeeper;
    this.tradeStatistic = tradeStatistic;
    this.liquidCash = startingCash;
    this.startingCash = startingCash;
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

  public TradeStatistic getTradeStatistic() {
    return tradeStatistic;
  }

  public Bookkeeper getBookkeeper() {
    return bookkeeper;
  }

  public Portfolio getPortfolio() {
    return portfolio;
  }

  public double getTotalAccountValue() {
    return NumUtil.plus(portfolio.getTotalEquityValue(), liquidCash);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("liquidCash", liquidCash).toString();
  }

  @Override
  public void onFulfilledBuy(final BuyTransaction txn, final BarData barData) {
    bookkeeper.keep(txn);
  }

  @Override
  public void onFulfilledSell(final SellTransaction txn, final BarData barData) {
    bookkeeper.keep(txn);
    tradeStatistic.collectStats(txn);
  }
}

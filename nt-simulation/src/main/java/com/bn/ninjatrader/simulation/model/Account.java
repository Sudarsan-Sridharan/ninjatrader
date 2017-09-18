package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.listener.BrokerListener;
import com.bn.ninjatrader.simulation.model.portfolio.Portfolio;
import com.bn.ninjatrader.simulation.model.stat.TradeStatistic;
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
  private final TradeStatistic tradeStatistic;

  private double liquidCash = 100000;
  private double startingCash = 100000;

  private Account() {
    this(null, null, 0d);
  }

  public Account(final Portfolio portfolio,
                 final TradeStatistic tradeStatistic,
                 final double startingCash) {
    this.portfolio = portfolio;
    this.tradeStatistic = tradeStatistic;
    this.liquidCash = startingCash;
    this.startingCash = startingCash;
  }

  public double getLiquidCash() {
    return liquidCash;
  }

  public double getProfit() {
    double profit = NumUtil.minus(liquidCash, startingCash);
    return NumUtil.trim(profit, 2);
  }

  public TradeStatistic getTradeStatistic() {
    return tradeStatistic;
  }

  public Portfolio getPortfolio() {
    return portfolio;
  }

  @Override
  public void onFulfilledBuy(final BuyTransaction txn, final BarData barData) {
    tradeStatistic.collectStats(txn);
    portfolio.add(txn);
    liquidCash -= txn.getValue();
  }

  @Override
  public void onFulfilledSell(final SellTransaction txn, final BarData barData) {
    tradeStatistic.collectStats(txn);
    liquidCash += txn.getValue();
  }

  @Override
  public void onCleanup(final SellTransaction txn, final BarData barData) {
    tradeStatistic.collectStats(txn);
    liquidCash += txn.getValue();
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("portfolio", portfolio)
        .add("tradeStatistic", tradeStatistic)
        .add("liquidCash", liquidCash)
        .add("startingCash", startingCash)
        .toString();
  }
}

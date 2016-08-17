package com.bn.ninjatrader.testplay.simulation.broker;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.testplay.simulation.account.Portfolio;
import com.bn.ninjatrader.testplay.simulation.account.VirtualAccount;
import com.bn.ninjatrader.testplay.simulation.log.TradeLog;
import com.bn.ninjatrader.testplay.simulation.log.TradeLogger;
import com.bn.ninjatrader.testplay.simulation.order.BuyOrder;
import com.bn.ninjatrader.testplay.simulation.order.Order;
import com.bn.ninjatrader.testplay.simulation.order.OrderType;
import com.bn.ninjatrader.testplay.simulation.stats.TradeStatistic;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Brad on 8/12/16.
 */
public class Broker {

  private static final Logger log = LoggerFactory.getLogger(Broker.class);

  private List<Order> pendingOrders = Lists.newArrayList();
  private final VirtualAccount account;
  private final TradeLogger tradeLogger = new TradeLogger();
  private final TradeStatistic tradeStatistic = new TradeStatistic();

  public Broker(VirtualAccount account) {
    Preconditions.checkNotNull(account);
    this.account = account;
  }

  public void submitOrder(Order order) {
    Preconditions.checkNotNull(order);
    pendingOrders.add(order);
  }

  public void processPendingOrders(Price price) {
    Preconditions.checkNotNull(price);

    if (pendingOrders.isEmpty()) {
      return;
    }

    List<Order> fulfilledOrders = Lists.newArrayList();
    for (Order order : pendingOrders) {
      order.decrementDaysFromNow();
      if (order.isReadyForProcessing()) {
        fulfillOrder(order, price);
        fulfilledOrders.add(order);
      }
    }
    pendingOrders.removeAll(fulfilledOrders);
  }

  private void fulfillOrder(Order order, Price price) {
    if (order.getOrderType() == OrderType.BUY) {
      fulfillBuyOrder(order, price);
    } else if (order.getOrderType() == OrderType.SELL) {
      fulfillSellOrder(order, price);
    }
  }

  private void fulfillBuyOrder(Order order, Price currentPrice) {
    BuyOrder buyOrder = (BuyOrder) order;
    buyOrder.fulfill(currentPrice);
    double buyPrice = order.getFulfilledPrice();
    long numOfShares = (long)(buyOrder.getBuyAmount() / buyPrice / 1000) * 1000;
    double totalValue = NumUtil.multiply(buyOrder.getNumOfShares(), buyPrice);

    account.getPortfolio().add(buyOrder);
    account.addCash(-totalValue);

    tradeLogger.logBuy(order.getOrderDate(), buyPrice, numOfShares);
  }

  private void fulfillSellOrder(Order order, Price currentPrice) {
    Portfolio portfolio = account.getPortfolio();

    order.fulfill(currentPrice);
    double sellPrice = order.getFulfilledPrice();
    double avgBoughtPrice = portfolio.getAvgPrice();
    long totalShares = portfolio.getTotalShares();
    double totalValue = NumUtil.multiply(sellPrice, totalShares);
    double priceDiff = sellPrice - avgBoughtPrice;
    double profit = NumUtil.multiply(priceDiff, totalShares);

    account.addCash(totalValue);
    portfolio.clear();

    tradeLogger.logSell(order.getOrderDate(), sellPrice, totalShares);
    tradeStatistic.trade();
    if (profit > 0) {
      tradeStatistic.win();
    } else {
      tradeStatistic.lose();
    }
  }

  public void printStats() {
    double cash = account.getCash();
    double startingCash = account.getStartingCash();
    double profit = NumUtil.minus(cash, startingCash);

    log.info("******* Trade Logs *********");
    for (TradeLog tradeLog : tradeLogger.getTradeLogs()) {
      log.info("{}", tradeLog.toString());
    }
    log.info("\n");
    log.info("Number of Trades: {}", tradeStatistic.getNumOfTrades());
    log.info("Number of Wins: {}", tradeStatistic.getNumOfWins());
    log.info("Number of Losses: {}", tradeStatistic.getNumOfLosses());
    log.info("Win / Loss Ratio: {}", tradeStatistic.getWinLoseRation());
    log.info("Starting Equity: {}", startingCash);
    log.info("Ending Equity: {}", (long) cash);
    log.info("Profit: {}", (long) profit);
    log.info("% Gain: {}%", (long) (profit / startingCash * 100));
    log.info("Profit per Trade: {}", tradeStatistic.getProfitPerTrade(profit));
  }
}

package com.bn.ninjatrader.testplay.simulation.account;

import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.testplay.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.testplay.simulation.transaction.SellTransaction;
import com.bn.ninjatrader.testplay.simulation.transaction.Bookkeeper;
import com.bn.ninjatrader.testplay.simulation.transaction.Transaction;
import com.bn.ninjatrader.testplay.simulation.stats.TradeStatistic;
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
  private final Bookkeeper bookkeeper = new Bookkeeper();
  private final TradeStatistic tradeStatistic = new TradeStatistic();

  public static Account startWithCash(double cash) {
    Account account = new Account();
    account.cash = cash;
    account.startingCash = cash;
    return account;
  }

  private Account() {}

  public void onBuySuccess(BuyTransaction transaction) {
    bookkeeper.keep(transaction);
  }

  public void onSellSuccess(SellTransaction transaction) {
    bookkeeper.keep(transaction);
    tradeStatistic.collect(transaction);
  }

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

  public Bookkeeper getBookkeeper() {
    return bookkeeper;
  }

  public TradeStatistic getTradeStatistic() {
    return tradeStatistic;
  }

  public void printStats() {
    double profit = NumUtil.minus(cash, startingCash);

    log.info("******* Trade Logs *********");
    for (Transaction transaction : bookkeeper.getTransactions()) {
      log.info("{}", transaction.toString());
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

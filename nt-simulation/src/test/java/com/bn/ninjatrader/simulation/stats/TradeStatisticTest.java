package com.bn.ninjatrader.simulation.stats;

import com.bn.ninjatrader.simulation.transaction.SellTransaction;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 8/19/16.
 */
public class TradeStatisticTest {

  private TradeStatistic tradeStatistic;

  private SellTransaction winningTrade = Transaction.sell().profit(100).build();
  private SellTransaction losingTrade = Transaction.sell().profit(-100).build();

  @BeforeMethod
  public void setup() {
    tradeStatistic = new TradeStatistic();
  }

  @Test
  public void testOnCreate() {
    assertEquals(tradeStatistic.getNumOfTrades(), 0);
    assertEquals(tradeStatistic.getNumOfWins(), 0);
    assertEquals(tradeStatistic.getNumOfLosses(), 0);
    assertEquals(tradeStatistic.getWinPcnt(), Double.NaN);
    assertEquals(tradeStatistic.getLossPcnt(), Double.NaN);
    assertEquals(tradeStatistic.getWinLoseRatio(), Double.NaN);
    assertEquals(tradeStatistic.getProfitPerTrade(), Double.NaN);
  }

  @Test
  public void testWinStats() {
    tradeStatistic.collectStats(winningTrade);

    assertEquals(tradeStatistic.getNumOfTrades(), 1);
    assertEquals(tradeStatistic.getNumOfWins(), 1);
    assertEquals(tradeStatistic.getNumOfLosses(), 0);
    assertEquals(tradeStatistic.getWinPcnt(), 100.0);
    assertEquals(tradeStatistic.getLossPcnt(), 0.0);
    assertEquals(tradeStatistic.getWinLoseRatio(), Double.POSITIVE_INFINITY);
    assertEquals(tradeStatistic.getProfitPerTrade(), 100.0);
  }

  @Test
  public void testLossStats() {
    tradeStatistic.collectStats(losingTrade);

    assertEquals(tradeStatistic.getNumOfTrades(), 1);
    assertEquals(tradeStatistic.getNumOfWins(), 0);
    assertEquals(tradeStatistic.getNumOfLosses(), 1);
    assertEquals(tradeStatistic.getWinPcnt(), 0.0);
    assertEquals(tradeStatistic.getLossPcnt(), 100.0);
    assertEquals(tradeStatistic.getWinLoseRatio(), 0.0);
    assertEquals(tradeStatistic.getProfitPerTrade(), -100.0);
  }

  @Test
  public void testMultiTradeStats() {
    tradeStatistic.collectStats(winningTrade);
    tradeStatistic.collectStats(winningTrade);
    tradeStatistic.collectStats(losingTrade);
    tradeStatistic.collectStats(losingTrade);
    tradeStatistic.collectStats(winningTrade);

    assertEquals(tradeStatistic.getNumOfTrades(), 5);
    assertEquals(tradeStatistic.getNumOfWins(), 3);
    assertEquals(tradeStatistic.getNumOfLosses(), 2);
    assertEquals(tradeStatistic.getWinPcnt(), 60.0);
    assertEquals(tradeStatistic.getLossPcnt(), 40.0);
    assertEquals(tradeStatistic.getWinLoseRatio(), 1.5);
    assertEquals(tradeStatistic.getProfitPerTrade(), 20.0);
  }
}

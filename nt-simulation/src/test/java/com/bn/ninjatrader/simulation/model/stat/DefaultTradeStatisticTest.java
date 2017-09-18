package com.bn.ninjatrader.simulation.model.stat;

import com.bn.ninjatrader.simulation.transaction.SellTransaction;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by Brad on 8/19/16.
 */
public class DefaultTradeStatisticTest {

  private TradeStatistic tradeStatistic;

  private SellTransaction winningTrade = Transaction.sell().profit(100).build();
  private SellTransaction losingTrade = Transaction.sell().profit(-100).build();

  @Before
  public void setup() {
    tradeStatistic = new DefaultTradeStatistic();
  }

  @Test
  public void testOnCreate() {
    assertThat(tradeStatistic.getNumOfTrades()).isEqualTo(0);
    assertThat(tradeStatistic.getNumOfWins()).isEqualTo(0);
    assertThat(tradeStatistic.getNumOfLosses()).isEqualTo(0);
    assertThat(tradeStatistic.getWinPcnt()).isEqualTo(Double.NaN);
    assertThat(tradeStatistic.getLossPcnt()).isEqualTo(Double.NaN);
    assertThat(tradeStatistic.getProfitPerTrade()).isEqualTo(Double.NaN);
  }

  @Test
  public void testWinStats() {
    tradeStatistic.collectStats(winningTrade);

    assertThat(tradeStatistic.getNumOfTrades()).isEqualTo(1);
    assertThat(tradeStatistic.getNumOfWins()).isEqualTo(1);
    assertThat(tradeStatistic.getNumOfLosses()).isEqualTo(0);
    assertThat(tradeStatistic.getWinPcnt()).isEqualTo(1.0);
    assertThat(tradeStatistic.getLossPcnt()).isEqualTo(0.0);
    assertThat(tradeStatistic.getProfitPerTrade()).isEqualTo(100.0);
  }

  @Test
  public void testLossStats() {
    tradeStatistic.collectStats(losingTrade);

    assertThat(tradeStatistic.getNumOfTrades()).isEqualTo(1);
    assertThat(tradeStatistic.getNumOfWins()).isEqualTo(0);
    assertThat(tradeStatistic.getNumOfLosses()).isEqualTo(1);
    assertThat(tradeStatistic.getWinPcnt()).isEqualTo(0.0);
    assertThat(tradeStatistic.getLossPcnt()).isEqualTo(1.0);
    assertThat(tradeStatistic.getProfitPerTrade()).isEqualTo(-100.0);
  }

  @Test
  public void testMultiTradeStats() {
    tradeStatistic.collectStats(winningTrade);
    tradeStatistic.collectStats(winningTrade);
    tradeStatistic.collectStats(losingTrade);
    tradeStatistic.collectStats(losingTrade);
    tradeStatistic.collectStats(winningTrade);

    assertThat(tradeStatistic.getNumOfTrades()).isEqualTo(5);
    assertThat(tradeStatistic.getNumOfWins()).isEqualTo(3);
    assertThat(tradeStatistic.getNumOfLosses()).isEqualTo(2);
    assertThat(tradeStatistic.getWinPcnt()).isEqualTo(0.6);
    assertThat(tradeStatistic.getLossPcnt()).isEqualTo(0.4);
    assertThat(tradeStatistic.getProfitPerTrade()).isEqualTo(20.0);
  }
}

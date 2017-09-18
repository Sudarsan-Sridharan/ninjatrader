package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.portfolio.Portfolio;
import com.bn.ninjatrader.simulation.model.stat.TradeStatistic;
import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.simulation.transaction.SellTransaction;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Brad on 8/23/16.
 */
public class AccountTest {

  private Account account;
  private TradeStatistic tradeStatistic;
  private Portfolio portfolio;

  @Before
  public void setup() {
    tradeStatistic = mock(TradeStatistic.class);
    portfolio = mock(Portfolio.class);

    account = new Account(portfolio, tradeStatistic, 100000);
  }

  @Test
  public void testOnCreate_shouldSetDefaults() {
    assertThat(account.getLiquidCash()).isEqualTo(100000.0);
    assertThat(account.getProfit()).isEqualTo(0.0);
  }

  @Test
  public void testOnFulfilledBuy_shouldAddSharesToPortfolio() {
    final BuyTransaction buyTxn = mock(BuyTransaction.class);
    final BarData barData = mock(BarData.class);

    when(buyTxn.getValue()).thenReturn(10000d);

    account.onFulfilledBuy(buyTxn, barData);

    assertThat(account.getLiquidCash()).isEqualTo(90000d);
    assertThat(account.getLiquidCash()).isEqualTo(90000d);

    verify(tradeStatistic).collectStats(buyTxn);
    verify(portfolio).add(buyTxn);
  }

  @Test
  public void testOnFulfilledSell_shouldRemoveSharesFromPortfolioAndAddCash() {
    final SellTransaction sellTxn = mock(SellTransaction.class);
    final BarData barData = mock(BarData.class);

    when(sellTxn.getValue()).thenReturn(10000d);

    account.onFulfilledSell(sellTxn, barData);

    assertThat(account.getLiquidCash()).isEqualTo(110000d);
    assertThat(account.getProfit()).isEqualTo(10000d);

    verify(tradeStatistic).collectStats(sellTxn);
  }

  @Test
  public void testCalcProfit_shouldIncludeUnsoldEquity() {
    final BuyTransaction buyTxn = mock(BuyTransaction.class);
    final BarData barData = mock(BarData.class);

    when(buyTxn.getNumOfShares()).thenReturn(1000l);
    when(buyTxn.getPrice()).thenReturn(10d);

    account.onFulfilledBuy(buyTxn, barData);
  }
}

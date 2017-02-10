package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.simulation.transaction.SellTransaction;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by Brad on 8/23/16.
 */
public class AccountTest {

  private Account account;
  private Bookkeeper bookkeeper;
  private TradeStatistic tradeStatistic;
  private Portfolio portfolio;

  @Before
  public void setup() {
    bookkeeper = mock(Bookkeeper.class);
    tradeStatistic = mock(TradeStatistic.class);
    portfolio = mock(Portfolio.class);

    account = new Account(portfolio, bookkeeper, tradeStatistic, 100000);
  }

  @Test
  public void testOnCreate_shouldSetDefaults() {
    assertThat(account.getLiquidCash()).isEqualTo(100000.0);
    assertThat(account.getPortfolio().getAvgPrice()).isEqualTo(0.0);
    assertThat(account.getPortfolio().getTotalShares()).isEqualTo(0);
    assertThat(account.getProfit()).isEqualTo(0.0);
  }

  @Test
  public void testAddCash_shouldAddCash() {
    account.addCash(1000);
    assertThat(account.getProfit()).isEqualTo(1000.0);
    assertThat(account.getLiquidCash()).isEqualTo(101000.0);

    account.addCash(-2000);
    assertThat(account.getProfit()).isEqualTo(-1000.0);
    assertThat(account.getLiquidCash()).isEqualTo(99000.0);
  }

  @Test
  public void testOnFulfilledBuy_shouldAddSharesToPortfolio() {
    final BuyTransaction buyTxn = mock(BuyTransaction.class);
    final BarData barData = mock(BarData.class);

    when(buyTxn.getValue()).thenReturn(10000d);

    account.onFulfilledBuy(buyTxn, barData);

    assertThat(account.getLiquidCash()).isEqualTo(90000d);

    verify(bookkeeper).keep(buyTxn);
  }

  @Test
  public void testOnFulfilledSell_shouldRemoveSharesFromPortfolioAndAddCash() {
    final SellTransaction sellTxn = mock(SellTransaction.class);
    final BarData barData = mock(BarData.class);

    when(sellTxn.getValue()).thenReturn(10000d);

    account.onFulfilledSell(sellTxn, barData);

    assertThat(account.getLiquidCash()).isEqualTo(110000d);

    verify(portfolio).clear();
    verify(bookkeeper).keep(sellTxn);
    verify(tradeStatistic).collectStats(sellTxn);

  }
}

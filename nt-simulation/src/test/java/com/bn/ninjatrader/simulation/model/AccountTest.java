package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 8/23/16.
 */
public class AccountTest {

  private BuyTransaction buy1 = Transaction.buy().price(1.0).shares(10000).build();
  private BuyTransaction buy2 = Transaction.buy().price(2.0).shares(1000).build();

  private Account account;

  @Before
  public void setup() {
    account = Account.withStartingCash(100000);
  }

  @Test
  public void testOnCreate_shouldSetDefaults() {
    assertThat(account.getLiquidCash()).isEqualTo(100000.0);
    assertThat(account.getPortfolio().getAvgPrice()).isEqualTo(0.0);
    assertThat(account.getPortfolio().getTotalShares()).isEqualTo(0);
    assertThat(account.getProfit()).isEqualTo(0.0);
    assertThat(account.hasShares()).isFalse();
  }

  @Test
  public void testAddToPortfolio_shouldAddSharesToPortfolio() {
    account.addToPortfolio(buy1);
    assertThat(account.getLiquidCash()).isEqualTo(100000.0);
    assertThat(account.getPortfolio().getAvgPrice()).isEqualTo(1.0);
    assertThat(account.getPortfolio().getTotalShares()).isEqualTo(10000);
    assertThat(account.getProfit()).isEqualTo(0.0);
    assertThat(account.hasShares()).isTrue();

    account.addToPortfolio(buy2);
    assertThat(account.getLiquidCash()).isEqualTo(100000.0);
    assertThat(account.getPortfolio().getAvgPrice()).isEqualTo(1.090909);
    assertThat(account.getPortfolio().getTotalShares()).isEqualTo(11000);
    assertThat(account.getProfit()).isEqualTo(0.0);
    assertThat(account.hasShares()).isTrue();
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
  public void testClearPortfolio() {
    account.addToPortfolio(buy1);
    account.addToPortfolio(buy2);
    account.clearPortfolio();

    assertThat(account.hasShares()).isFalse();
  }
}

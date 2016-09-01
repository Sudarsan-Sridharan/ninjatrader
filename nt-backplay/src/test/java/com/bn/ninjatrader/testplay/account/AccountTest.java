package com.bn.ninjatrader.testplay.account;

import com.bn.ninjatrader.testplay.simulation.account.Account;
import com.bn.ninjatrader.testplay.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.testplay.simulation.transaction.Transaction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Created by Brad on 8/23/16.
 */
public class AccountTest {

  private BuyTransaction buy1 = Transaction.buy().price(1.0).shares(10000).build();
  private BuyTransaction buy2 = Transaction.buy().price(2.0).shares(1000).build();

  private Account account;

  @BeforeMethod
  public void setup() {
    account = Account.withStartingCash(100000);
  }

  @Test
  public void testOnCreate() {
    assertEquals(account.getCash(), 100000.0);
    assertEquals(account.getAvgPrice(), 0.0);
    assertEquals(account.getNumOfShares(), 0);
    assertEquals(account.getProfit(), 0.0);
    assertFalse(account.hasShares());
  }

  @Test
  public void testAddToPortfolio() {
    account.addToPortfolio(buy1);
    assertEquals(account.getCash(), 100000.0);
    assertEquals(account.getAvgPrice(), 1.0);
    assertEquals(account.getNumOfShares(), 10000);
    assertEquals(account.getProfit(), 0.0);
    assertTrue(account.hasShares());

    account.addToPortfolio(buy2);
    assertEquals(account.getCash(), 100000.0);
    assertEquals(account.getAvgPrice(), 1.090909);
    assertEquals(account.getNumOfShares(), 11000);
    assertEquals(account.getProfit(), 0.0);
    assertTrue(account.hasShares());
  }

  @Test
  public void testAddCash() {
    account.addCash(1000);
    assertEquals(account.getProfit(), 1000.0);
    assertEquals(account.getCash(), 101000.0);

    account.addCash(-2000);
    assertEquals(account.getProfit(), -1000.0);
    assertEquals(account.getCash(), 99000.0);
  }

  @Test
  public void testClearPortfolio() {
    account.addToPortfolio(buy1);
    account.addToPortfolio(buy2);
    account.clearPortfolio();

    assertFalse(account.hasShares());
  }
}

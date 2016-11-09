package com.bn.ninjatrader.simulation.account;

import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Created by Brad on 8/23/16.
 */
public class PortfolioTest {

  private BuyTransaction buy1 = Transaction.buy().price(1).shares(10000).build();
  private BuyTransaction buy2 = Transaction.buy().price(2).shares(10000).build();
  private BuyTransaction buy3 = Transaction.buy().price(3).shares(1000).build();

  private Portfolio portfolio;

  @BeforeMethod
  public void setup() {
    portfolio = new Portfolio();
  }

  @Test
  public void testOnCreate() {
    assertPortfolioIsEmpty();
  }

  @Test
  public void testAdd() {
    portfolio.add(buy1);
    assertFalse(portfolio.isEmpty());
  }

  @Test
  public void testAvgPrice() {
    portfolio.add(buy1);
    assertEquals(portfolio.getAvgPrice(), 1.0);

    portfolio.add(buy2);
    assertEquals(portfolio.getAvgPrice(), 1.5);

    portfolio.add(buy3);
    assertEquals(portfolio.getAvgPrice(), 1.571429);
  }

  @Test
  public void testNumOfShares() {
    portfolio.add(buy1);
    assertEquals(portfolio.getTotalShares(), 10000);

    portfolio.add(buy2);
    assertEquals(portfolio.getTotalShares(), 20000);

    portfolio.add(buy3);
    assertEquals(portfolio.getTotalShares(), 21000);
  }

  @Test
  public void testClear() {
    portfolio.add(buy1);
    portfolio.clear();
    assertPortfolioIsEmpty();
  }

  private void assertPortfolioIsEmpty() {
    assertTrue(portfolio.isEmpty());
    assertEquals(portfolio.getAvgPrice(), 0.0);
    assertEquals(portfolio.getTotalShares(), 0);
  }
}

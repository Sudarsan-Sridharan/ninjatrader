package com.bn.ninjatrader.testplay.simulation.transaction;

import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * Created by Brad on 8/17/16.
 */
public class SellTransactionTest {

  private LocalDate now = LocalDate.of(2016, 1, 1);

  @Test
  public void testCreateEmpty() {
    SellTransaction transaction = Transaction.sell().build();

    assertNull(transaction.getDate());
    assertEquals(transaction.getPrice(), 0.0);
    assertEquals(transaction.getNumOfShares(), 0);
    assertEquals(transaction.getProfit(), 0.0);
    assertEquals(transaction.getTransactionType(), TransactionType.SELL);
  }

  @Test
  public void testCreateWithData() {
    SellTransaction transaction = Transaction.sell().date(now).price(100).shares(1000).profit(5000).build();

    assertEquals(transaction.getDate(), now);
    assertEquals(transaction.getPrice(), 100.0);
    assertEquals(transaction.getNumOfShares(), 1000);
    assertEquals(transaction.getProfit(), 5000.0);
    assertEquals(transaction.getTransactionType(), TransactionType.SELL);
  }
}

package com.bn.ninjatrader.testplay.simulation.transaction;

import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * Created by Brad on 8/17/16.
 */
public class BuyTransactionTest {

  private LocalDate now = LocalDate.of(2016, 1, 1);

  @Test
  public void testCreateEmpty() {
    BuyTransaction transaction = Transaction.buy().build();

    assertNull(transaction.getDate());
    assertEquals(transaction.getPrice(), 0.0);
    assertEquals(transaction.getNumOfShares(), 0);
    assertEquals(transaction.getTransactionType(), TransactionType.BUY);
  }

  @Test
  public void testCreateWithData() {
    BuyTransaction transaction = Transaction.buy().date(now).price(100).shares(1000).build();

    assertEquals(transaction.getDate(), now);
    assertEquals(transaction.getPrice(), 100.0);
    assertEquals(transaction.getNumOfShares(), 1000);
    assertEquals(transaction.getTransactionType(), TransactionType.BUY);
  }
}

package com.bn.ninjatrader.testplay.simulation.transaction;

import com.bn.ninjatrader.common.util.TestUtil;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertTrue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;

/**
 * Created by Brad on 8/17/16.
 */
public class SellTransactionTest {

  private LocalDate now = LocalDate.of(2016, 1, 1);
  private LocalDate tom = LocalDate.of(2016, 1, 2);

  private SellTransaction sell1 = Transaction.sell().price(1).shares(10).date(now).barIndex(1).profit(1000).build();
  private SellTransaction sell2 = Transaction.sell().price(1).shares(10).date(now).barIndex(1).profit(1000).build();
  private SellTransaction sell3 = Transaction.sell().price(2).shares(10).date(now).barIndex(1).profit(1000).build();
  private SellTransaction sell4 = Transaction.sell().price(1).shares(11).date(now).barIndex(1).profit(1000).build();
  private SellTransaction sell5 = Transaction.sell().price(1).shares(10).date(tom).barIndex(1).profit(1000).build();
  private SellTransaction sell6 = Transaction.sell().price(1).shares(10).date(now).barIndex(2).profit(1000).build();
  private SellTransaction sell7 = Transaction.sell().price(1).shares(10).date(now).barIndex(1).profit(1001).build();


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

  @Test
  public void testCalculateValue() {
    SellTransaction transaction = Transaction.sell().date(now).price(100).shares(1000).build();
    assertEquals(transaction.getValue(), 100000d);

    transaction = Transaction.sell().date(now).price(0.0001).shares(1000).build();
    assertEquals(transaction.getValue(), 0.1);
  }

  @Test
  public void testHashCode() {
    assertEquals(sell1.hashCode(), sell2.hashCode());

    TestUtil.assertHashCodeNotEquals(sell1, sell3, sell4, sell5, sell6, sell7);
    TestUtil.assertHashCodeNotEquals(sell2, sell3, sell4, sell5, sell6, sell7);
  }

  @Test
  public void testEquals() {
    assertTrue(sell1.equals(sell1));
    assertTrue(sell1.equals(sell2));
    assertTrue(sell2.equals(sell1));

    assertFalse(sell1.equals(new Object()));
    assertFalse(sell1.equals(null));

    TestUtil.assertNotEqualsList(sell1, sell3, sell4, sell5, sell6, sell7);
    TestUtil.assertNotEqualsList(sell2, sell3, sell4, sell5, sell6, sell7);
  }
}

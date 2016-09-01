package com.bn.ninjatrader.testplay.simulation.transaction;

import com.bn.ninjatrader.common.util.TestUtil;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertTrue;
import static org.testng.Assert.*;

/**
 * Created by Brad on 8/17/16.
 */
public class BuyTransactionTest {

  private LocalDate now = LocalDate.of(2016, 1, 1);
  private LocalDate tomorrow = LocalDate.of(2016, 1, 2);

  private BuyTransaction buy1 = Transaction.buy().price(1).shares(10).date(now).barIndex(1).build();
  private BuyTransaction buy2 = Transaction.buy().price(1).shares(10).date(now).barIndex(1).build();
  private BuyTransaction buy3 = Transaction.buy().price(2).shares(10).date(now).barIndex(1).build();
  private BuyTransaction buy4 = Transaction.buy().price(1).shares(11).date(now).barIndex(1).build();
  private BuyTransaction buy5 = Transaction.buy().price(1).shares(10).date(tomorrow).barIndex(1).build();
  private BuyTransaction buy6 = Transaction.buy().price(1).shares(10).date(now).barIndex(2).build();
  private SellTransaction sell1 = Transaction.sell().price(1).shares(10).date(now).barIndex(1).build();

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

  @Test
  public void testCalculateValue() {
    BuyTransaction transaction = Transaction.buy().date(now).price(100).shares(1000).build();
    assertEquals(transaction.getValue(), 100000d);

    transaction = Transaction.buy().date(now).price(0.0001).shares(1000).build();
    assertEquals(transaction.getValue(), 0.1);
  }

  @Test
  public void testHashCode() {
    assertEquals(buy1.hashCode(), buy2.hashCode());

    assertNotEquals(buy1.hashCode(), sell1.hashCode());

    TestUtil.assertHashCodeNotEquals(buy1, buy3, buy4, buy5, buy6);
    TestUtil.assertHashCodeNotEquals(buy2, buy3, buy4, buy5, buy6);
  }

  @Test
  public void testEquals() {
    assertTrue(buy1.equals(buy1));
    assertTrue(buy1.equals(buy2));
    assertTrue(buy2.equals(buy1));

    assertFalse(buy1.equals(new Object()));
    assertFalse(buy1.equals(null));
    assertFalse(buy1.equals(sell1));

    TestUtil.assertNotEqualsList(buy1, buy3, buy4, buy5, buy6);
    TestUtil.assertNotEqualsList(buy2, buy3, buy4, buy5, buy6);
  }
}

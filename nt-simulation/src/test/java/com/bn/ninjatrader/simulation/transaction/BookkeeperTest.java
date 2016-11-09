package com.bn.ninjatrader.simulation.transaction;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created by Brad on 8/11/16.
 */
public class BookkeeperTest {

  private Bookkeeper bookkeeper;
  private LocalDate now;

  @BeforeMethod
  public void setup() {
    now = LocalDate.of(2016, 1, 1);
    bookkeeper = new Bookkeeper();
  }
  
  @Test
  public void testCreate() {
    Bookkeeper bookkeeper = new Bookkeeper();
    assertNotNull(bookkeeper.getTransactions());
    assertEquals(bookkeeper.getNumOfTrades(), 0);
  }

  @Test
  public void testSimpleBuy() {
    bookkeeper.keep(Transaction.buy().date(now).price(1.3).shares(300).build());
    assertEquals(bookkeeper.getNumOfTrades(), 1);

    List<Transaction> logs = bookkeeper.getTransactions();
    assertNotNull(logs);
    assertEquals(logs.size(), 1);

    Transaction transaction = logs.get(0);
    assertEquals(transaction.getTransactionType(), TransactionType.BUY);
    assertEquals(transaction.getDate(), now);
    assertEquals(transaction.getNumOfShares(), 300);
    assertEquals(transaction.getPrice(), 1.3);
  }

  @Test
  public void testMultiLog() {
    Bookkeeper bookkeeper = new Bookkeeper();

    bookkeeper.keep(Transaction.buy().date(now).price(1).shares(300).build());
    assertEquals(bookkeeper.getNumOfTrades(), 1);

    bookkeeper.keep(Transaction.sell().date(now).price(2.1).shares(400).build());
    assertEquals(bookkeeper.getNumOfTrades(), 2);

    bookkeeper.keep(Transaction.buy().date(now).price(3.5).shares(500).build());
    assertEquals(bookkeeper.getNumOfTrades(), 3);

    Transaction transaction = bookkeeper.getTransactions().get(1);
    assertEquals(transaction.getTransactionType(), TransactionType.SELL);
    assertEquals(transaction.getDate(), now);
    assertEquals(transaction.getNumOfShares(), 400);
    assertEquals(transaction.getPrice(), 2.1);
  }
}

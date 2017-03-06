package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.simulation.transaction.Transaction;
import com.bn.ninjatrader.simulation.transaction.TransactionType;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by Brad on 8/11/16.
 */
public class BookkeeperTest {

  private Bookkeeper bookkeeper;
  private LocalDate now;

  @Before
  public void before() {
    now = LocalDate.of(2016, 1, 1);
    bookkeeper = new Bookkeeper();
  }
  
  @Test
  public void testCreate() {
    Bookkeeper bookkeeper = new Bookkeeper();
    assertThat(bookkeeper.getTransactions()).isNotNull();
    assertThat(bookkeeper.getNumOfTrades()).isEqualTo(0);
  }

  @Test
  public void testSimpleBuy() {
    bookkeeper.keep(Transaction.buy().date(now).price(1.3).shares(300).build());
    assertThat(bookkeeper.getNumOfTrades()).isEqualTo(1);

    final List<Transaction> logs = bookkeeper.getTransactions();
    assertThat(logs).isNotNull();
    assertThat(logs.size()).isEqualTo(1);

    final Transaction transaction = logs.get(0);
    assertThat(transaction.getTransactionType()).isEqualTo(TransactionType.BUY);
    assertThat(transaction.getDate()).isEqualTo(now);
    assertThat(transaction.getNumOfShares()).isEqualTo(300);
    assertThat(transaction.getPrice()).isEqualTo(1.3);
  }

  @Test
  public void testMultiLog() {
    Bookkeeper bookkeeper = new Bookkeeper();

    bookkeeper.keep(Transaction.buy().date(now).price(1).shares(300).build());
    assertThat(bookkeeper.getNumOfTrades()).isEqualTo(1);

    bookkeeper.keep(Transaction.sell().date(now).price(2.1).shares(400).build());
    assertThat(bookkeeper.getNumOfTrades()).isEqualTo(2);

    bookkeeper.keep(Transaction.buy().date(now).price(3.5).shares(500).build());
    assertThat(bookkeeper.getNumOfTrades()).isEqualTo(3);

    Transaction transaction = bookkeeper.getTransactions().get(1);
    assertThat(transaction.getTransactionType()).isEqualTo(TransactionType.SELL);
    assertThat(transaction.getDate()).isEqualTo(now);
    assertThat(transaction.getNumOfShares()).isEqualTo(400);
    assertThat(transaction.getPrice()).isEqualTo(2.1);
  }
}

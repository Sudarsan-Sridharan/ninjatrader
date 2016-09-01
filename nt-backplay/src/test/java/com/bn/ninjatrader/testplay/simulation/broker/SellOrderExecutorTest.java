package com.bn.ninjatrader.testplay.simulation.broker;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.testplay.simulation.account.Account;
import com.bn.ninjatrader.testplay.simulation.data.BarData;
import com.bn.ninjatrader.testplay.simulation.order.MarketTime;
import com.bn.ninjatrader.testplay.simulation.order.Order;
import com.bn.ninjatrader.testplay.simulation.transaction.SellTransaction;
import com.bn.ninjatrader.testplay.simulation.transaction.Transaction;
import com.bn.ninjatrader.testplay.simulation.transaction.TransactionType;
import mockit.Tested;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created by Brad on 8/18/16.
 */
public class SellOrderExecutorTest {

  @Tested
  private SellOrderExecutor executor;

  private Account account;

  private final LocalDate now = LocalDate.of(2016, 1, 1);
  private final Price price = new Price(now, 1, 2, 3, 4, 1000);
  private final BarData barData = BarData.forPrice(price);
  private final Order order = Order.buy().cashAmount(100000).at(MarketTime.OPEN).build();

  @BeforeMethod
  public void setup() {
    account = Account.withStartingCash(0);
    account.addToPortfolio(Transaction.buy().price(0.5).shares(100000).build());
  }

  @Test
  public void testExecute() {
    SellTransaction transaction = executor.execute(account, order, barData);

    assertValidTransaction(transaction);
    assertAccountPortfolioSold();
  }

  private void assertValidTransaction(SellTransaction transaction) {
    assertNotNull(transaction);
    assertEquals(transaction.getDate(), now);
    assertEquals(transaction.getNumOfShares(), 100000);
    assertEquals(transaction.getTransactionType(), TransactionType.SELL);
    assertEquals(transaction.getValue(), 100000.0);
  }

  private void assertAccountPortfolioSold() {
    assertEquals(account.getCash(), 100000.0);
    assertEquals(account.getNumOfShares(), 0);
    assertEquals(account.getAvgPrice(), 0.0);
  }
}

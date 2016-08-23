package com.bn.ninjatrader.testplay.simulation.order;

import com.bn.ninjatrader.testplay.simulation.transaction.TransactionType;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static com.bn.ninjatrader.testplay.simulation.order.MarketTime.CLOSE;
import static org.testng.Assert.*;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by Brad on 8/17/16.
 */
public class BuyOrderTest {

  private LocalDate now = LocalDate.of(2016, 1, 1);

  @Test
  public void testCreateEmpty() {
    BuyOrder order = Order.buy().build();
    assertNull(order.getOrderDate());
    assertEquals(order.getCashAmount(), 0.0);
    assertEquals(order.getNumOfShares(), 0);
    assertEquals(order.getTransactionType(), TransactionType.BUY);
  }

  @Test
  public void testCreateWithData() {
    BuyOrder order = Order.buy().date(now).at(CLOSE).shares(1000).cashAmount(20000).daysFromNow(5).build();
    assertEquals(order.getOrderDate(), now);
    assertEquals(order.getMarketTime(), CLOSE);
    assertEquals(order.getNumOfShares(), 1000);
    assertEquals(order.getCashAmount(), 20000.0);
    assertEquals(order.getTransactionType(), TransactionType.BUY);
    assertFalse(order.isReadyForProcessing());
  }

  @Test
  public void testReadyForProcessing() {
    BuyOrder order = Order.buy().date(now).at(CLOSE).shares(1000).daysFromNow(1).build();
    assertFalse(order.isReadyForProcessing());

    order.decrementDaysFromNow();
    assertTrue(order.isReadyForProcessing());
  }
}

package com.bn.ninjatrader.simulation.order;

import com.bn.ninjatrader.simulation.transaction.TransactionType;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static com.bn.ninjatrader.simulation.order.MarketTime.CLOSE;
import static com.bn.ninjatrader.simulation.order.MarketTime.OPEN;
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
    BuyOrder order = Order.buy().date(now).at(CLOSE).shares(1000).cashAmount(20000).barsFromNow(5).build();
    assertEquals(order.getOrderDate(), now);
    assertEquals(order.getMarketTime(), CLOSE);
    assertEquals(order.getNumOfShares(), 1000);
    assertEquals(order.getCashAmount(), 20000.0);
    assertEquals(order.getTransactionType(), TransactionType.BUY);
    assertFalse(order.isReadyForProcessing());
  }

  @Test
  public void testReadyForProcessing() {
    BuyOrder order = Order.buy().date(now).at(CLOSE).shares(1000).barsFromNow(1).build();
    assertFalse(order.isReadyForProcessing());

    order.decrementDaysFromNow();
    assertTrue(order.isReadyForProcessing());
  }

  @Test
  public void testWithParams() {
    BuyOrderParameters params = OrderParameters.buy().at(OPEN).barsFromNow(100).build();
    BuyOrder order = Order.buy().params(params).build();

    assertEquals(order.getMarketTime(), OPEN);
    assertEquals(order.getBarsFromNow(), 100);
  }
}

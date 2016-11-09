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
public class SellOrderTest {

  private LocalDate now = LocalDate.of(2016, 1, 1);

  @Test
  public void testCreateEmpty() {
    SellOrder order = Order.sell().build();
    assertNull(order.getOrderDate());
    assertEquals(order.getNumOfShares(), 0);
    assertEquals(order.getTransactionType(), TransactionType.SELL);
  }

  @Test
  public void testCreateWithData() {
    SellOrder order = Order.sell().date(now).at(CLOSE).shares(1000).barsFromNow(5).build();
    assertEquals(order.getOrderDate(), now);
    assertEquals(order.getMarketTime(), CLOSE);
    assertEquals(order.getNumOfShares(), 1000);
    assertEquals(order.getTransactionType(), TransactionType.SELL);
    assertFalse(order.isReadyForProcessing());
  }

  @Test
  public void testReadyForProcessing() {
    SellOrder order = Order.sell().date(now).at(CLOSE).shares(1000).barsFromNow(1).build();
    assertFalse(order.isReadyForProcessing());

    order.decrementDaysFromNow();
    assertTrue(order.isReadyForProcessing());
  }

  @Test
  public void testWithParams() {
    SellOrderParameters params = OrderParameters.sell().at(OPEN).barsFromNow(100).build();
    SellOrder order = Order.sell().params(params).build();

    assertEquals(order.getMarketTime(), OPEN);
    assertEquals(order.getBarsFromNow(), 100);
  }
}

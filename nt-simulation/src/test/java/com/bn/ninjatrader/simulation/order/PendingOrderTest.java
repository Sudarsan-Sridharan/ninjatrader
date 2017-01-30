package com.bn.ninjatrader.simulation.order;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.order.type.OrderType;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class PendingOrderTest {

  private Order order;
  private OrderType orderType;
  private BarData currentBarData;
  private BarData boughtAtBarData;

  @Before
  public void before() {
    order = mock(Order.class);
    orderType = mock(OrderType.class);
    currentBarData = mock(BarData.class);
    boughtAtBarData = mock(BarData.class);

    when(order.getOrderType()).thenReturn(orderType);
    when(boughtAtBarData.getIndex()).thenReturn(1);
    when(currentBarData.getIndex()).thenReturn(1);
  }

  @Test
  public void testCreate_shouldSetProperties() {
    final PendingOrder pendingOrder = PendingOrder.of(order, boughtAtBarData);
    assertThat(pendingOrder.getBarData()).isEqualTo(boughtAtBarData);
    assertThat(pendingOrder.getOrder()).isEqualTo(order);
  }

  @Test
  public void testIsReadyForProcessingWithBarsFromNow_shouldReturnTrueIfBarsFromNowIsMatched() {
    when(order.getBarsFromNow()).thenReturn(5);
    when(orderType.isFulfillable(any(BarData.class))).thenReturn(true);
    when(boughtAtBarData.getIndex()).thenReturn(1);
    when(currentBarData.getIndex()).thenReturn(5);

    final PendingOrder pendingOrder = PendingOrder.of(order, boughtAtBarData);
    assertThat(pendingOrder.isReadyToProcess(currentBarData)).isFalse();

    when(currentBarData.getIndex()).thenReturn(6);
    assertThat(pendingOrder.isReadyToProcess(currentBarData)).isTrue();
  }

  @Test
  public void testIsReadyForProcessingWithOrderType_shouldReturnTrueIfOrderTypeConditionIsMatched() {
    when(orderType.isFulfillable(any(BarData.class))).thenReturn(false);

    assertThat(PendingOrder.of(order, boughtAtBarData).isReadyToProcess(currentBarData)).isFalse();

    when(orderType.isFulfillable(any(BarData.class))).thenReturn(true);

    assertThat(PendingOrder.of(order, boughtAtBarData).isReadyToProcess(currentBarData)).isTrue();
  }
}

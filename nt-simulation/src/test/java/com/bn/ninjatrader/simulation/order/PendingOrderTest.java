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
  private OrderConfig orderConfig;
  private BarData currentBarData;
  private BarData boughtAtBarData;

  @Before
  public void before() {
    order = mock(Order.class);
    orderType = mock(OrderType.class);
    orderConfig = mock(OrderConfig.class);
    currentBarData = mock(BarData.class);
    boughtAtBarData = mock(BarData.class);

    when(order.getOrderType()).thenReturn(orderType);
    when(order.getOrderConfig()).thenReturn(orderConfig);
    when(order.getSymbol()).thenReturn("MEG");
    when(order.getNumOfShares()).thenReturn(1000l);

    when(boughtAtBarData.getIndex()).thenReturn(1);
    when(currentBarData.getIndex()).thenReturn(1);
  }

  @Test
  public void testCreate_shouldSetProperties() {
    final PendingOrder pendingOrder = PendingOrder.of(order, boughtAtBarData);
    assertThat(pendingOrder.getSubmittedBarData()).isEqualTo(boughtAtBarData);
    assertThat(pendingOrder.getOrderType()).isEqualTo(orderType);
    assertThat(pendingOrder.getOrderConfig()).isEqualTo(orderConfig);
    assertThat(pendingOrder.getSymbol()).isEqualTo("MEG");
    assertThat(pendingOrder.getNumOfShares()).isEqualTo(1000);
  }

  @Test
  public void testIsReadyForProcessingWithBarsFromNow_shouldReturnTrueIfBarsFromNowIsMatched() {
    when(orderConfig.getBarsFromNow()).thenReturn(5);
    when(orderType.isFulfillable(any(BarData.class), any(BarData.class))).thenReturn(true);
    when(boughtAtBarData.getIndex()).thenReturn(1);
    when(currentBarData.getIndex()).thenReturn(5);

    final PendingOrder pendingOrder = PendingOrder.of(order, boughtAtBarData);
    assertThat(pendingOrder.isReadyToProcess(currentBarData)).isFalse();

    when(currentBarData.getIndex()).thenReturn(6);
    assertThat(pendingOrder.isReadyToProcess(currentBarData)).isTrue();
  }

  @Test
  public void testIsReadyForProcessingWithOrderType_shouldReturnTrueIfOrderTypeConditionIsMatched() {
    when(orderType.isFulfillable(any(BarData.class), any(BarData.class))).thenReturn(false);

    assertThat(PendingOrder.of(order, boughtAtBarData).isReadyToProcess(currentBarData)).isFalse();

    when(orderType.isFulfillable(any(BarData.class), any(BarData.class))).thenReturn(true);

    assertThat(PendingOrder.of(order, boughtAtBarData).isReadyToProcess(currentBarData)).isTrue();
  }

  @Test
  public void testIsExpired_shouldReturnTrueIfCurrentBarIndexIsAfterExpiry() {
    // 10 <= 2 + MAX_VALUE = not expired
    when(orderConfig.getExpireAfterNumOfBars()).thenReturn(Integer.MAX_VALUE);
    when(boughtAtBarData.getIndex()).thenReturn(2);
    when(currentBarData.getIndex()).thenReturn(10);
    assertThat(PendingOrder.of(order, boughtAtBarData).isExpired(currentBarData)).isFalse();

    // 11 <= 1 + 10 = not expired
    when(orderConfig.getExpireAfterNumOfBars()).thenReturn(10);
    when(boughtAtBarData.getIndex()).thenReturn(1);
    when(currentBarData.getIndex()).thenReturn(11);
    assertThat(PendingOrder.of(order, boughtAtBarData).isExpired(currentBarData)).isFalse();

    // 12 < 1 + 10 = false, expired
    when(orderConfig.getExpireAfterNumOfBars()).thenReturn(10);
    when(boughtAtBarData.getIndex()).thenReturn(1);
    when(currentBarData.getIndex()).thenReturn(12);
    assertThat(PendingOrder.of(order, boughtAtBarData).isExpired(currentBarData)).isTrue();
  }
}

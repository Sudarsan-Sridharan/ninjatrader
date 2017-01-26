package com.bn.ninjatrader.simulation.order;

import com.bn.ninjatrader.simulation.data.BarData;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class PendingOrderTest {

  private Order order;
  private BarData currentBarData;
  private BarData boughtAtBarData;

  @Before
  public void before() {
    order = mock(Order.class);
    currentBarData = mock(BarData.class);
    boughtAtBarData = mock(BarData.class);
  }

  @Test
  public void testCreate_shouldSetProperties() {
    final PendingOrder pendingOrder = PendingOrder.of(order, boughtAtBarData);
    assertThat(pendingOrder.getBarData()).isEqualTo(boughtAtBarData);
    assertThat(pendingOrder.getOrder()).isEqualTo(order);
  }

  @Test
  public void testIsReadyForProcessing_shouldBeTrueIfBarsFromNowIsMatched() {
    when(order.getBarsFromNow()).thenReturn(5);
    when(boughtAtBarData.getIndex()).thenReturn(1000);
    when(currentBarData.getIndex()).thenReturn(1001);

    final PendingOrder pendingOrder = PendingOrder.of(order, boughtAtBarData);
    assertThat(pendingOrder.isReadyToProcess(currentBarData)).isFalse();

    when(currentBarData.getIndex()).thenReturn(1005);
    assertThat(pendingOrder.isReadyToProcess(currentBarData)).isTrue();
  }
}

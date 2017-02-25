package com.bn.ninjatrader.simulation.order.cancel;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.Broker;
import com.bn.ninjatrader.simulation.model.World;
import com.bn.ninjatrader.simulation.order.PendingOrder;
import com.bn.ninjatrader.simulation.util.DummyObjectMapperProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class CancelAllTest {

  private BarData barData;
  private World world;
  private Broker broker;
  private PendingOrder pendingOrder1;
  private PendingOrder pendingOrder2;

  private CancelAll cancelAll = CancelAll.instance();

  @Before
  public void before() {
    barData = mock(BarData.class);
    world = mock(World.class);
    broker = mock(Broker.class);
    pendingOrder1 = mock(PendingOrder.class);
    pendingOrder2 = mock(PendingOrder.class);

    when(barData.getWorld()).thenReturn(world);
    when(world.getBroker()).thenReturn(broker);
    when(broker.getPendingOrders()).thenReturn(Lists.newArrayList(pendingOrder1, pendingOrder2));
  }

  @Test
  public void testFindPendingOrders_shouldFindAll() {
    assertThat(cancelAll.findPendingOrdersToCancel(barData)).containsExactly(pendingOrder1, pendingOrder2);
  }

  @Test
  public void testSerializeDeserialize_shouldProduceEqualObject() throws IOException {
    final ObjectMapper om = DummyObjectMapperProvider.get();
    final String json = om.writeValueAsString(cancelAll);
    assertThat(om.readValue(json, CancelType.class)).isEqualTo(cancelAll);
  }
}

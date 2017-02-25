package com.bn.ninjatrader.simulation.statement;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.Account;
import com.bn.ninjatrader.simulation.model.Broker;
import com.bn.ninjatrader.simulation.model.World;
import com.bn.ninjatrader.simulation.order.PendingOrder;
import com.bn.ninjatrader.simulation.order.cancel.CancelAll;
import com.bn.ninjatrader.simulation.order.cancel.CancelType;
import com.bn.ninjatrader.simulation.util.DummyObjectMapperProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static com.bn.ninjatrader.simulation.order.cancel.CancelTypes.cancelAll;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author bradwee2000@gmail.com
 */
public class CancelOrderStatementTest {

  private final CancelOrderStatement orig = CancelOrderStatement.builder().cancelType(cancelAll()).build();
  private final CancelOrderStatement equal = CancelOrderStatement.builder().cancelType(cancelAll()).build();
  private final CancelOrderStatement diffType = CancelOrderStatement.builder()
      .cancelType(mock(CancelType.class)).build();

  private World world;
  private Account account;
  private Broker broker;
  private BarData barData;
  private PendingOrder pendingOrder1;
  private PendingOrder pendingOrder2;
  private List<PendingOrder> pendingOrders;

  @Before
  public void before() {
    world = mock(World.class);
    account = mock(Account.class);
    broker = mock(Broker.class);
    barData = mock(BarData.class);
    pendingOrder1 = mock(PendingOrder.class);
    pendingOrder2 = mock(PendingOrder.class);
    pendingOrders = Lists.newArrayList(pendingOrder1, pendingOrder2);

    when(barData.getWorld()).thenReturn(world);
    when(world.getBroker()).thenReturn(broker);
    when(broker.hasPendingOrder()).thenReturn(true);
    when(broker.getPendingOrders()).thenReturn(pendingOrders);
  }

  @Test
  public void testBuild_shouldSetProperties() {
    assertThat(orig.getCancelType()).isEqualTo(CancelAll.instance());
  }

  @Test
  public void testRunWithCancelAll_shouldCancelAllPendingOrders() {
    orig.run(barData);

    // Verify orders are cancelled.
    verify(broker).removePendingOrders(pendingOrders);
  }

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(equal).isEqualTo(orig);
    assertThat(orig).isEqualTo(orig).isEqualTo(equal)
        .isNotEqualTo(null)
        .isNotEqualTo("")
        .isNotEqualTo(diffType);
  }

  @Test
  public void testHashcode_shouldHaveEqualHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, equal, diffType))
        .containsExactlyInAnyOrder(orig, diffType);
  }

  @Test
  public void testSerializeDeserialize_shouldProduceEqualObject() throws IOException {
    final ObjectMapper om = DummyObjectMapperProvider.get();
    final String json = om.writeValueAsString(orig);
    assertThat(om.readValue(json, Statement.class)).isEqualTo(orig);
  }
}

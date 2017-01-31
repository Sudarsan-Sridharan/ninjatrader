package com.bn.ninjatrader.simulation.statement;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.Account;
import com.bn.ninjatrader.simulation.model.Broker;
import com.bn.ninjatrader.simulation.model.World;
import com.bn.ninjatrader.simulation.order.BuyOrder;
import com.bn.ninjatrader.simulation.order.OrderConfig;
import com.bn.ninjatrader.simulation.order.type.OrderTypes;
import com.bn.ninjatrader.simulation.transaction.TransactionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author bradwee2000@gmail.com
 */
public class BuyOrderStatementTest {

  private final LocalDate now = LocalDate.of(2016, 2, 1);

  private final Price price = Price.builder().date(now).close(1.1).build();

  private final BuyOrderStatement orig = BuyOrderStatement.builder()
      .orderType(OrderTypes.marketClose()).build();
  private final BuyOrderStatement equal = BuyOrderStatement.builder()
      .orderType(OrderTypes.marketClose()).build();
  private final BuyOrderStatement diffMarketTime = BuyOrderStatement.builder()
      .orderType(OrderTypes.marketOpen()).build();
  private final BuyOrderStatement diffConfig = BuyOrderStatement.builder()
      .orderType(OrderTypes.marketClose()).orderConfig(OrderConfig.defaults().barsFromNow(1)).build();

  private World world;
  private Account account;
  private Broker broker;
  private BarData barData;

  @Before
  public void before() {
    world = mock(World.class);
    account = mock(Account.class);
    broker = mock(Broker.class);
    barData = mock(BarData.class);

    when(barData.getWorld()).thenReturn(world);
    when(barData.getPrice()).thenReturn(price);
    when(world.getBroker()).thenReturn(broker);
    when(world.getAccount()).thenReturn(account);
    when(account.getLiquidCash()).thenReturn(100000d);
  }

  @Test
  public void testBuild_shouldSetProperties() {
    assertThat(orig.getOrderType()).isEqualTo(OrderTypes.marketClose());
    assertThat(orig.getOrderConfig()).isEqualTo(OrderConfig.defaults());
    assertThat(diffConfig.getOrderConfig()).isEqualTo(OrderConfig.defaults().barsFromNow(1));
  }

  @Test
  public void testRunWithNoPendingOrders_shouldSubmitBuyOrder() {
    final ArgumentCaptor<BuyOrder> orderCaptor = ArgumentCaptor.forClass(BuyOrder.class);
    final ArgumentCaptor<BarData> barDataCaptor = ArgumentCaptor.forClass(BarData.class);

    // Broker has no pending orders
    when(broker.hasPendingOrder()).thenReturn(Boolean.FALSE);

    orig.run(barData);

    // Verify order submitted to broker
    verify(broker).submitOrder(orderCaptor.capture(), barDataCaptor.capture());

    final BuyOrder order = orderCaptor.getValue();
    assertThat(order.getCashAmount()).isEqualTo(100000d);
    assertThat(order.getOrderType()).isEqualTo(OrderTypes.marketClose());
    assertThat(order.getOrderDate()).isEqualTo(now);
    assertThat(order.getOrderConfig()).isEqualTo(OrderConfig.defaults());
    assertThat(order.getTransactionType()).isEqualTo(TransactionType.BUY);

    assertThat(barDataCaptor.getValue()).isEqualTo(barData);
  }

  @Test
  public void testRunPendingOrders_shouldNotSubmitBuyOrder() {
    // Broker has no pending orders
    when(broker.hasPendingOrder()).thenReturn(Boolean.TRUE);

    orig.run(barData);

    // Verify order submitted to broker
    verify(broker, times(0)).submitOrder(any(BuyOrder.class), any(BarData.class));
  }

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(equal).isEqualTo(orig);
    assertThat(orig).isEqualTo(orig).isEqualTo(equal)
        .isNotEqualTo(null)
        .isNotEqualTo("")
        .isNotEqualTo(diffMarketTime)
        .isNotEqualTo(diffConfig);
  }

  @Test
  public void testHashcode_shouldHaveEqualHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, equal, diffConfig, diffMarketTime))
        .containsExactlyInAnyOrder(orig, diffConfig, diffMarketTime);
  }

  @Test
  public void testSerializeDeserialize_shouldProduceEqualObject() throws IOException {
    final ObjectMapper om = TestUtil.objectMapper();
    final String json = om.writeValueAsString(orig);
    assertThat(om.readValue(json, BuyOrderStatement.class)).isEqualTo(orig);
  }
}

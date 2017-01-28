package com.bn.ninjatrader.simulation.statement;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.Account;
import com.bn.ninjatrader.simulation.model.Broker;
import com.bn.ninjatrader.simulation.model.World;
import com.bn.ninjatrader.simulation.order.SellOrder;
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
public class SellOrderStatementTest {

  private final LocalDate now = LocalDate.of(2016, 2, 1);
  private final Price price = Price.builder().date(now).close(1.1).build();
  private final BarData barData = BarData.builder().price(price).build();

  private final SellOrderStatement orig = SellOrderStatement.builder()
      .orderType(OrderTypes.marketClose()).barsFromNow(1).build();
  private final SellOrderStatement equal = SellOrderStatement.builder()
      .orderType(OrderTypes.marketClose()).barsFromNow(1).build();
  private final SellOrderStatement diffMarketTime = SellOrderStatement.builder()
      .orderType(OrderTypes.marketOpen()).barsFromNow(1).build();
  private final SellOrderStatement diffBarsFromNow = SellOrderStatement.builder()
      .orderType(OrderTypes.marketClose()).barsFromNow(2).build();

  private World world;
  private Account account;
  private Broker broker;

  @Before
  public void before() {
    world = mock(World.class);
    broker = mock(Broker.class);
    account = mock(Account.class);

    when(world.getBroker()).thenReturn(broker);
    when(world.getAccount()).thenReturn(account);
    when(account.getCash()).thenReturn(100000d);
  }

  @Test
  public void testBuild_shouldSetProperties() {
    assertThat(orig.getOrderType()).isEqualTo(OrderTypes.marketClose());
    assertThat(orig.getBarsFromNow()).isEqualTo(1);
  }

  @Test
  public void testRunWithSharesAvailable_shouldSubmitBuyOrder() {
    final ArgumentCaptor<SellOrder> orderCaptor = ArgumentCaptor.forClass(SellOrder.class);
    final ArgumentCaptor<BarData> barDataCaptor = ArgumentCaptor.forClass(BarData.class);
    final SellOrderStatement statement = SellOrderStatement.builder()
        .orderType(OrderTypes.marketOpen()).barsFromNow(1).build();

    // Account has shares to sell
    when(account.hasShares()).thenReturn(true);
    when(account.getNumOfShares()).thenReturn(100000l);

    statement.run(world, barData);

    // Verify order submitted to broker
    verify(broker).submitOrder(orderCaptor.capture(), barDataCaptor.capture());

    final SellOrder order = orderCaptor.getValue();
    assertThat(order.getOrderType()).isEqualTo(OrderTypes.marketOpen());
    assertThat(order.getOrderDate()).isEqualTo(now);
    assertThat(order.getBarsFromNow()).isEqualTo(1);
    assertThat(order.getTransactionType()).isEqualTo(TransactionType.SELL);

    assertThat(barDataCaptor.getValue()).isEqualTo(barData);
  }

  @Test
  public void testRunPendingOrders_shouldNotSubmitBuyOrder() {
    final SellOrderStatement statement = SellOrderStatement.builder()
        .orderType(OrderTypes.marketOpen()).barsFromNow(1).build();

    // Broker has no pending orders
    when(broker.hasPendingOrder()).thenReturn(Boolean.TRUE);

    statement.run(world, barData);

    // Verify order submitted to broker
    verify(broker, times(0)).submitOrder(any(SellOrder.class), any(BarData.class));
  }

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(equal).isEqualTo(orig);
    assertThat(orig).isEqualTo(orig).isEqualTo(equal)
        .isNotEqualTo(null)
        .isNotEqualTo("")
        .isNotEqualTo(diffMarketTime)
        .isNotEqualTo(diffBarsFromNow);
  }

  @Test
  public void testHashcode_shouldHaveEqualHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, equal, diffBarsFromNow, diffMarketTime))
        .containsExactlyInAnyOrder(orig, diffBarsFromNow, diffMarketTime);
  }

  @Test
  public void testSerializeDeserialize_shouldProduceEqualObject() throws IOException {
    final ObjectMapper om = TestUtil.objectMapper();
    final String json = om.writeValueAsString(orig);
    assertThat(om.readValue(json, SellOrderStatement.class)).isEqualTo(orig);
  }
}

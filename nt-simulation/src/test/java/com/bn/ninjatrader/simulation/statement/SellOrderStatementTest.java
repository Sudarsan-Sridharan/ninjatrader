package com.bn.ninjatrader.simulation.statement;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.Account;
import com.bn.ninjatrader.simulation.model.Broker;
import com.bn.ninjatrader.simulation.model.World;
import com.bn.ninjatrader.simulation.order.OrderConfig;
import com.bn.ninjatrader.simulation.order.SellOrder;
import com.bn.ninjatrader.simulation.transaction.TransactionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.time.LocalDate;

import static com.bn.ninjatrader.simulation.order.type.OrderTypes.marketClose;
import static com.bn.ninjatrader.simulation.order.type.OrderTypes.marketOpen;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author bradwee2000@gmail.com
 */
public class SellOrderStatementTest {

  private final LocalDate now = LocalDate.of(2016, 2, 1);
  private final Price price = Price.builder().date(now).close(1.1).build();

  private final SellOrderStatement orig = SellOrderStatement.builder().build();
  private final SellOrderStatement equal = SellOrderStatement.builder().orderType(marketClose()).build();
  private final SellOrderStatement diffMarketTime = SellOrderStatement.builder().orderType(marketOpen()).build();
  private final SellOrderStatement diffConfig = SellOrderStatement.builder()
      .orderType(marketClose()).orderConfig(OrderConfig.defaults().barsFromNow(1)).build();

  private World world;
  private Account account;
  private Broker broker;
  private BarData barData;

  @Before
  public void before() {
    world = mock(World.class);
    broker = mock(Broker.class);
    account = mock(Account.class);
    barData = mock(BarData.class);

    when(barData.getWorld()).thenReturn(world);
    when(barData.getPrice()).thenReturn(price);
    when(world.getBroker()).thenReturn(broker);
    when(world.getAccount()).thenReturn(account);
    when(account.getLiquidCash()).thenReturn(100000d);
  }

  @Test
  public void testBuild_shouldSetProperties() {
    assertThat(orig.getOrderType()).isEqualTo(marketClose());
    assertThat(orig.getOrderConfig()).isEqualTo(OrderConfig.defaults());
    assertThat(diffConfig.getOrderConfig()).isEqualTo(OrderConfig.defaults().barsFromNow(1));
  }

  @Test
  public void testRunWithSharesAvailable_shouldSubmitBuyOrder() {
    final ArgumentCaptor<SellOrder> orderCaptor = ArgumentCaptor.forClass(SellOrder.class);
    final ArgumentCaptor<BarData> barDataCaptor = ArgumentCaptor.forClass(BarData.class);
    final SellOrderStatement statement = SellOrderStatement.builder()
        .orderType(marketOpen()).orderConfig(OrderConfig.withBarsFromNow(1)).build();

    // Account has shares to sell
    when(account.hasShares()).thenReturn(true);
    when(account.getNumOfShares()).thenReturn(100000l);

    statement.run(barData);

    // Verify order submitted to broker
    verify(broker).submitOrder(orderCaptor.capture(), barDataCaptor.capture());

    final SellOrder order = orderCaptor.getValue();
    assertThat(order.getOrderType()).isEqualTo(marketOpen());
    assertThat(order.getOrderDate()).isEqualTo(now);
    assertThat(order.getOrderConfig().getBarsFromNow()).isEqualTo(1);
    assertThat(order.getTransactionType()).isEqualTo(TransactionType.SELL);

    assertThat(barDataCaptor.getValue()).isEqualTo(barData);
  }

  @Test
  public void testRunPendingOrders_shouldNotSubmitBuyOrder() {
    final SellOrderStatement statement = SellOrderStatement.builder()
        .orderType(marketOpen()).orderConfig(OrderConfig.withBarsFromNow(1)).build();

    // Broker has no pending orders
    when(broker.hasPendingOrder()).thenReturn(Boolean.TRUE);

    statement.run(barData);

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
    assertThat(om.readValue(json, SellOrderStatement.class)).isEqualTo(orig);
  }
}

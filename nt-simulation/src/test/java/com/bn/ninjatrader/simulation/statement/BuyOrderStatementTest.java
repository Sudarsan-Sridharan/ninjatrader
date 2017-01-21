package com.bn.ninjatrader.simulation.statement;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.simulation.account.Account;
import com.bn.ninjatrader.simulation.broker.Broker;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.World;
import com.bn.ninjatrader.simulation.order.BuyOrder;
import com.bn.ninjatrader.simulation.order.MarketTime;
import com.bn.ninjatrader.simulation.transaction.TransactionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.time.LocalDate;

import static com.bn.ninjatrader.simulation.order.MarketTime.CLOSE;
import static com.bn.ninjatrader.simulation.order.MarketTime.OPEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author bradwee2000@gmail.com
 */
public class BuyOrderStatementTest {

  private final LocalDate now = LocalDate.of(2016, 2, 1);
  private final BuyOrderStatement statement = BuyOrderStatement.builder()
      .marketTime(MarketTime.CLOSE).barsFromNow(1).build();
  private final Price price = Price.builder().date(now).close(1.1).build();
  private final BarData barData = BarData.builder().price(price).build();

  private final BuyOrderStatement orig = BuyOrderStatement.builder().marketTime(CLOSE).barsFromNow(1).build();
  private final BuyOrderStatement equal = BuyOrderStatement.builder().marketTime(CLOSE).barsFromNow(1).build();
  private final BuyOrderStatement diffMarketTime = BuyOrderStatement.builder()
      .marketTime(OPEN).barsFromNow(1).build();
  private final BuyOrderStatement diffBarsFromNow = BuyOrderStatement.builder()
      .marketTime(CLOSE).barsFromNow(2).build();

  private World world;
  private Account account;
  private Broker broker;

  @Before
  public void before() {
    world = mock(World.class);
    account = mock(Account.class);
    broker = mock(Broker.class);

    when(world.getBroker()).thenReturn(broker);
    when(world.getAccount()).thenReturn(account);
    when(account.getCash()).thenReturn(100000d);
  }

  @Test
  public void testBuild_shouldSetProperties() {
    assertThat(orig.getMarketTime()).isEqualTo(MarketTime.CLOSE);
    assertThat(orig.getBarsFromNow()).isEqualTo(1);
  }

  @Test
  public void testRunWithNoPendingOrders_shouldSubmitBuyOrder() {
    final ArgumentCaptor<BuyOrder> orderCaptor = ArgumentCaptor.forClass(BuyOrder.class);
    final ArgumentCaptor<BarData> barDataCaptor = ArgumentCaptor.forClass(BarData.class);

    // Broker has no pending orders
    when(broker.hasPendingOrder()).thenReturn(Boolean.FALSE);

    statement.run(world, barData);

    // Verify order submitted to broker
    verify(broker).submitOrder(orderCaptor.capture(), barDataCaptor.capture());

    final BuyOrder order = orderCaptor.getValue();
    assertThat(order.getCashAmount()).isEqualTo(100000d);
    assertThat(order.getMarketTime()).isEqualTo(MarketTime.CLOSE);
    assertThat(order.getOrderDate()).isEqualTo(now);
    assertThat(order.getBarsFromNow()).isEqualTo(1);
    assertThat(order.getTransactionType()).isEqualTo(TransactionType.BUY);

    assertThat(barDataCaptor.getValue()).isEqualTo(barData);
  }

  @Test
  public void testRunPendingOrders_shouldNotSubmitBuyOrder() {
    // Broker has no pending orders
    when(broker.hasPendingOrder()).thenReturn(Boolean.TRUE);

    statement.run(world, barData);

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
    assertThat(om.readValue(json, BuyOrderStatement.class)).isEqualTo(orig);
  }
}

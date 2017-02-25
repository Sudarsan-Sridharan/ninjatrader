package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.entity.PriceBuilderFactory;
import com.bn.ninjatrader.model.util.DummyPriceBuilderFactory;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.listener.BrokerListener;
import com.bn.ninjatrader.simulation.order.BuyOrder;
import com.bn.ninjatrader.simulation.order.PendingOrder;
import com.bn.ninjatrader.simulation.order.SellOrder;
import com.bn.ninjatrader.simulation.order.executor.BuyOrderExecutor;
import com.bn.ninjatrader.simulation.order.executor.OrderExecutor;
import com.bn.ninjatrader.simulation.order.executor.SellOrderExecutor;
import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.simulation.transaction.SellTransaction;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import com.bn.ninjatrader.simulation.transaction.TransactionType;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Brad on 8/23/16.
 */
public class BrokerTest {

  private final LocalDate now = LocalDate.of(2016, 1, 1);
  private final PriceBuilderFactory pbf = new DummyPriceBuilderFactory();
  private final Price price = pbf.builder().date(now).open(1).high(2).low(3).close(4).volume(1000).build();
  private final BarData barData = BarData.builder().price(price).build();
  private final BuyTransaction buyTransaction = Transaction.buy().price(4).shares(1000).date(now).build();
  private final SellTransaction sellTransaction = Transaction.sell().price(4).shares(1000).date(now).build();
  private final BuyOrder buyOrder = BuyOrder.builder().cashAmount(100000).build();
  private final SellOrder sellOrder = SellOrder.builder().date(now).shares(1000).build();

  private Broker broker;
  private BuyOrderExecutor buyOrderExecutor;
  private SellOrderExecutor sellOrderExecutor;
  private BrokerListener brokerListener;

  private Map<TransactionType, OrderExecutor> orderExecutors;

  @Before
  public void setup() {
    buyOrderExecutor = mock(BuyOrderExecutor.class);
    sellOrderExecutor = mock(SellOrderExecutor.class);
    brokerListener = mock(BrokerListener.class);

    orderExecutors = Maps.newHashMap();
    orderExecutors.put(TransactionType.BUY, buyOrderExecutor);
    orderExecutors.put(TransactionType.SELL, sellOrderExecutor);

    broker = new Broker(orderExecutors);
    broker.addListener(brokerListener);
  }

  @Test
  public void testCreateEmpty_shouldSetDefaults() {
    assertThat(broker.hasPendingOrder()).isFalse();
    assertThat(broker.getLastTransaction(TransactionType.BUY)).isNotPresent();
    assertThat(broker.getLastTransaction(TransactionType.SELL)).isNotPresent();
  }

  @Test
  public void testSubmitOrder_shouldAddToPendingOrders() {
    broker.submitOrder(buyOrder, barData);
    assertThat(broker.hasPendingOrder()).isTrue();
    assertThat(broker.getLastTransaction(TransactionType.BUY)).isNotPresent();
    assertThat(broker.getLastTransaction(TransactionType.SELL)).isNotPresent();
  }

  @Test
  public void testProcessPendingOrder_shouldProcessPendingOrders() {
    when(buyOrderExecutor.execute(any(PendingOrder.class), any(BarData.class)))
        .thenReturn(buyTransaction);

    broker.submitOrder(buyOrder, barData);
    broker.processPendingOrders(barData);

    assertThat(broker.hasPendingOrder()).isFalse();
    assertThat(broker.getLastTransaction(TransactionType.BUY)).isPresent();
    assertThat(broker.getLastTransaction(TransactionType.SELL)).isNotPresent();

    final Transaction buy = broker.getLastTransaction(TransactionType.BUY).get();
    assertThat(buy).isEqualTo(buyTransaction);
  }

  @Test
  public void testRemovePendingOrder_shouldRemovePendingOrders() {
    broker.submitOrder(buyOrder, barData);
    broker.submitOrder(BuyOrder.builder().cashAmount(1000).build(), barData);

    assertThat(broker.getPendingOrders()).hasSize(2);

    broker.removePendingOrders(broker.getPendingOrders());

    assertThat(broker.getPendingOrders()).isEmpty();
  }

  @Test
  public void testListenerOnBuyFulfilled_shouldCallBuyFulfilledOnListeners() {
    when(buyOrderExecutor.execute(any(PendingOrder.class), any(BarData.class)))
        .thenReturn(buyTransaction);

    broker.submitOrder(buyOrder, barData);
    broker.processPendingOrders(barData);

    verify(brokerListener).onFulfilledBuy(buyTransaction, barData);
  }

  @Test
  public void testListenerOnSellFulfilled_shouldCallSellFulfilledOnListeners() {
    when(sellOrderExecutor.execute(any(PendingOrder.class), any(BarData.class)))
        .thenReturn(sellTransaction);

    broker.submitOrder(sellOrder, barData);
    broker.processPendingOrders(barData);

    verify(brokerListener).onFulfilledSell(sellTransaction, barData);
  }
}

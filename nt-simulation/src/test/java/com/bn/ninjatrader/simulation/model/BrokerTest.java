package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.common.model.Price;
import com.bn.ninjatrader.simulation.core.SimulationRequest;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.listener.BrokerListener;
import com.bn.ninjatrader.simulation.order.BuyOrder;
import com.bn.ninjatrader.simulation.order.OrderConfig;
import com.bn.ninjatrader.simulation.order.PendingOrder;
import com.bn.ninjatrader.simulation.order.SellOrder;
import com.bn.ninjatrader.simulation.order.executor.BuyOrderExecutor;
import com.bn.ninjatrader.simulation.order.executor.OrderExecutor;
import com.bn.ninjatrader.simulation.order.executor.SellOrderExecutor;
import com.bn.ninjatrader.simulation.order.processor.BuyOrderRequestProcessor;
import com.bn.ninjatrader.simulation.order.processor.OrderRequestProcessor;
import com.bn.ninjatrader.simulation.order.processor.SellOrderRequestProcessor;
import com.bn.ninjatrader.simulation.order.request.OrderRequestFactory;
import com.bn.ninjatrader.simulation.order.type.OrderTypes;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Brad on 8/23/16.
 */
public class BrokerTest {

  private final LocalDate now = LocalDate.of(2016, 1, 1);
  private final Price price = Price.builder().date(now).open(1).high(2).low(3).close(4).volume(1000).build();

  private final BuyTransaction buyTransaction = Transaction.buy().price(4).shares(1000).date(now).build();
  private final SellTransaction sellTransaction = Transaction.sell().price(4).shares(1000).date(now).build();
  private final BuyOrder buyOrder = BuyOrder.builder().cashAmount(100000).symbol("MEG").build();
  private final SellOrder sellOrder = SellOrder.builder().date(now).shares(1000).build();

  private Account account;
  private Portfolio portfolio;
  private Broker broker;
  private BuyOrderExecutor buyOrderExecutor;
  private SellOrderExecutor sellOrderExecutor;
  private BuyOrderRequestProcessor buyOrderRequestProcessor;
  private SellOrderRequestProcessor sellOrderRequestProcessor;
  private BrokerListener brokerListener;
  private BarData barData;
  private SimulationContext simulationContext;
  private OrderRequestFactory orderRequestFactory;

  private Map<TransactionType, OrderExecutor> orderExecutors;
  private Map<TransactionType, OrderRequestProcessor> orderRequestProcessors;

  @Before
  public void setup() {
    portfolio = mock(Portfolio.class);
    account = mock(Account.class);
    simulationContext = mock(SimulationContext.class);
    barData = mock(BarData.class);
    orderRequestFactory = mock(OrderRequestFactory.class);
    buyOrderExecutor = mock(BuyOrderExecutor.class);
    sellOrderExecutor = mock(SellOrderExecutor.class);
    buyOrderRequestProcessor = mock(BuyOrderRequestProcessor.class);
    sellOrderRequestProcessor = mock(SellOrderRequestProcessor.class);
    brokerListener = mock(BrokerListener.class);

    orderExecutors = Maps.newHashMap();
    orderExecutors.put(TransactionType.BUY, buyOrderExecutor);
    orderExecutors.put(TransactionType.SELL, sellOrderExecutor);

    orderRequestProcessors = Maps.newHashMap();
    orderRequestProcessors.put(TransactionType.BUY, buyOrderRequestProcessor);
    orderRequestProcessors.put(TransactionType.SELL, sellOrderRequestProcessor);

    broker = new Broker(orderExecutors, orderRequestProcessors, orderRequestFactory, mock(SimulationRequest.class));
    broker.addListener(brokerListener);

    when(barData.getPrice()).thenReturn(price);
    when(barData.getSimulationContext()).thenReturn(simulationContext);
    when(simulationContext.getAccount()).thenReturn(account);
    when(account.getPortfolio()).thenReturn(portfolio);
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
  public void testProcessExpiredOrder_shouldRemoveFromPendingOrders() {
    final BarData barTomorrow = mock(BarData.class);
    final BarData bar2DaysFromNow = mock(BarData.class);

    when(barData.getIndex()).thenReturn(0);

    when(barTomorrow.getPrice()).thenReturn(price);
    when(barTomorrow.getIndex()).thenReturn(2);
    when(barTomorrow.getSimulationContext()).thenReturn(simulationContext);

    when(bar2DaysFromNow.getPrice()).thenReturn(price);
    when(bar2DaysFromNow.getIndex()).thenReturn(3);
    when(bar2DaysFromNow.getSimulationContext()).thenReturn(simulationContext);

    // Create unfulfillable order with expiry
    final BuyOrder order = BuyOrder.builder()
        .symbol("MEG")
        .cashAmount(100000)
        .config(OrderConfig.withExpireAfterNumOfBars(2))
        .type(OrderTypes.atPrice(9999))
        .build();

    broker.submitOrder(order, barData);

    // Process tomorrow's bar
    broker.processPendingOrders(barTomorrow);

    // Nothing happens. Order is still pending
    assertThat(broker.hasPendingOrder()).isTrue();

    // Process bar of 2 days from now
    broker.processPendingOrders(bar2DaysFromNow);

    // Order is expired and removed from pending
    assertThat(broker.hasPendingOrder()).isFalse();
  }

  @Test
  public void testExpiredSellOrder_shouldCancelCommittedShares() {
    final BarData bar2DaysFromNow = mock(BarData.class);

    when(barData.getIndex()).thenReturn(0);

    when(bar2DaysFromNow.getPrice()).thenReturn(price);
    when(bar2DaysFromNow.getIndex()).thenReturn(3);
    when(bar2DaysFromNow.getSimulationContext()).thenReturn(simulationContext);

    // Create unfulfillable order with expiry
    final SellOrder order = SellOrder.builder()
        .symbol("MEG")
        .config(OrderConfig.withExpireAfterNumOfBars(2))
        .type(OrderTypes.atPrice(9999))
        .build();

    broker.submitOrder(order, barData);

    // Process bar of 2 days from now
    broker.processPendingOrders(bar2DaysFromNow);

    // Order is expired and removed from pending
    assertThat(broker.hasPendingOrder()).isFalse();

    // Verify committed shares are cancelled
    verify(portfolio).cancelCommittedShares("MEG", 0);
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

package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.order.*;
import com.bn.ninjatrader.simulation.order.executor.BuyOrderExecutor;
import com.bn.ninjatrader.simulation.order.executor.OrderExecutor;
import com.bn.ninjatrader.simulation.order.executor.SellOrderExecutor;
import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
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
import static org.mockito.Mockito.when;

/**
 * Created by Brad on 8/23/16.
 */
public class BrokerTest {

  private final LocalDate now = LocalDate.of(2016, 1, 1);
  private final Price price = new Price(now, 1, 2, 3, 4, 1000);
  private final BarData barData = BarData.builder().price(price).build();
  private final BuyTransaction buyTransaction = Transaction.buy().price(4).shares(1000).date(now).build();
  private final BuyOrder buyOrder = Order.buy().cashAmount(100000).build();

  private Broker broker;
  private Account account;
  private BuyOrderExecutor buyOrderExecutor;
  private SellOrderExecutor sellOrderExecutor;

  private Map<TransactionType, OrderExecutor> orderExecutors;

  @Before
  public void setup() {
    buyOrderExecutor = mock(BuyOrderExecutor.class);
    sellOrderExecutor = mock(SellOrderExecutor.class);

    orderExecutors = Maps.newHashMap();
    orderExecutors.put(TransactionType.BUY, buyOrderExecutor);
    orderExecutors.put(TransactionType.SELL, sellOrderExecutor);

    account = Account.withStartingCash(100000);
    broker = new Broker(account, orderExecutors);
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
    when(buyOrderExecutor.execute(any(Account.class), any(PendingOrder.class), any(BarData.class)))
        .thenReturn(buyTransaction);

    broker.submitOrder(buyOrder, barData);
    broker.processPendingOrders(barData);

    assertThat(broker.hasPendingOrder()).isFalse();
    assertThat(broker.getLastTransaction(TransactionType.BUY)).isPresent();
    assertThat(broker.getLastTransaction(TransactionType.SELL)).isNotPresent();

    final Transaction buy = broker.getLastTransaction(TransactionType.BUY).get();
    assertThat(buy).isEqualTo(buyTransaction);
  }
}

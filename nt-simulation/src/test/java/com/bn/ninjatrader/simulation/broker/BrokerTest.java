package com.bn.ninjatrader.simulation.broker;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.account.Account;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.order.BuyOrder;
import com.bn.ninjatrader.simulation.order.Order;
import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import com.google.inject.Guice;
import com.google.inject.Injector;
import mockit.Expectations;
import mockit.Mocked;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 8/23/16.
 */
public class BrokerTest {

  @Mocked
  private BuyOrderExecutor buyOrderExecutor;

  @Mocked
  private SellOrderExecutor sellOrderExecutor;

  private final LocalDate now = LocalDate.of(2016, 1, 1);
  private final Price price = new Price(now, 1, 2, 3, 4, 1000);
  private final BarData barData;
  private final BuyTransaction buyTransaction = Transaction.buy().price(4).shares(1000).date(now).build();
  private final BuyOrder buyOrder = Order.buy().cashAmount(100000).build();

  private Broker broker;
  private Account account;

  public BrokerTest() {
    barData = new BarData();
    barData.setPrice(price);
  }

  @BeforeMethod
  public void setup() {
    account = Account.withStartingCash(100000);
    broker = new Broker(account);

    Injector injector = Guice.createInjector();
    injector.injectMembers(broker);
  }

  @Test
  public void testCreateEmpty() {
    assertThat(broker.hasPendingOrder()).isFalse();
    assertThat(broker.getLastFulfilledBuy().isPresent()).isFalse();
    assertThat(broker.getLastFulfilledSell().isPresent()).isFalse();
  }

  @Test
  public void testSubmitOrder() {
    broker.submitOrder(buyOrder);
    assertThat(broker.hasPendingOrder()).isTrue();
    assertThat(broker.getLastFulfilledBuy().isPresent()).isFalse();
    assertThat(broker.getLastFulfilledSell().isPresent()).isFalse();
  }

  @Test
  public void testProcessPendingOrder() {
    new Expectations() {{
      buyOrderExecutor.execute(account, buyOrder, barData);
      result = buyTransaction;
    }};

    broker.submitOrder(buyOrder);
    broker.processPendingOrders(barData);

    assertThat(broker.hasPendingOrder()).isFalse();
    assertThat(broker.getLastFulfilledBuy()).isPresent();
    assertThat(broker.getLastFulfilledSell()).isNotPresent();

    BuyTransaction buy = broker.getLastFulfilledBuy().get();
    assertThat(buy).isEqualTo(buyTransaction);
  }
}

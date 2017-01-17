package com.bn.ninjatrader.simulation.core;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.account.Account;
import com.bn.ninjatrader.simulation.broker.Broker;
import com.bn.ninjatrader.simulation.condition.Condition;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.order.*;
import com.bn.ninjatrader.simulation.transaction.Bookkeeper;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


/**
 * @author bradwee2000@gmail.com
 */
public class SimulationTest {
  private static final Logger LOG = LoggerFactory.getLogger(SimulationTest.class);

  private Account account;
  private Bookkeeper bookkeeper;
  private Broker broker;
  private Condition buyCondition;
  private Condition sellCondition;
  private BuyOrderParameters buyOrderParameters;
  private SellOrderParameters sellOrderParameters;

  private LocalDate date1 = LocalDate.of(2016, 2, 1);
  private LocalDate date2 = LocalDate.of(2016, 2, 2);
  private Price price1 = new Price(date1, 1.2, 1.4, 1.1, 1.3, 100000);
  private Price price2 = new Price(date1, 2.2, 2.4, 2.1, 2.3, 200000);
  private List<Price> prices = Lists.newArrayList(price1, price2);

  private SimulationParams params;

  @BeforeMethod
  public void before() {
    account = mock(Account.class);
    bookkeeper = mock(Bookkeeper.class);
    broker = mock(Broker.class);
    buyCondition = mock(Condition.class);
    sellCondition = mock(Condition.class);
    buyOrderParameters = mock(BuyOrderParameters.class);
    sellOrderParameters = mock(SellOrderParameters.class);

    when(account.getBookkeeper()).thenReturn(bookkeeper);
    when(account.getCash()).thenReturn(100000d);

    params = new SimulationParams();
    params.setBuyCondition(buyCondition);
    params.setSellCondition(sellCondition);
    params.setBuyOrderParams(buyOrderParameters);
    params.setSellOrderParams(sellOrderParameters);
    params.setStartingCash(100000);
    params.setFromDate(date1);
    params.setToDate(date2);
    params.setSymbol("MEG");
  }

  @Test
  public void testPlay_shouldCreateBarData() {
    ArgumentCaptor<BarData> barDataCaptor = ArgumentCaptor.forClass(BarData.class);

    Simulation simulation = new Simulation(account, broker, params, Lists.newArrayList(price1, price2));
    simulation.play();

    // Should create bar data twice. One for each price.
    verify(broker, times(2)).processPendingOrders(barDataCaptor.capture());
    List<BarData> barDataList = barDataCaptor.getAllValues();
    assertThat(barDataList.get(0).getPrice()).isEqualTo(price1);
    assertThat(barDataList.get(1).getPrice()).isEqualTo(price2);
  }

  @Test
  public void testBuy_shouldSubmitBuyOrder() {
    Simulation simulation = new Simulation(account, broker, params, Lists.newArrayList(price1));
    ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

    // Trigger a buy. Buy on same day at market close.
    when(buyCondition.isMatch(any())).thenReturn(true);
    when(buyOrderParameters.getBarsFromNow()).thenReturn(0);
    when(buyOrderParameters.getMarketTime()).thenReturn(MarketTime.CLOSE);
    when(account.hasShares()).thenReturn(false, true); // Will have shares after buy.

    simulation.play();

    // Should submit order twice. One for the buy, another for the sell at end of simulation.
    verify(broker, times(2)).submitOrder(orderCaptor.capture());

    // Verify order details are correct
    BuyOrder buyOrder = (BuyOrder) orderCaptor.getAllValues().get(0);
    assertThat(buyOrder).isEqualTo(Order.buy()
        .cashAmount(100000)
        .barsFromNow(0)
        .at(MarketTime.CLOSE)
        .date(date1)
        .build());
  }

  @Test
  public void testSell_shouldSubmitSellOrder() {
    Simulation simulation = new Simulation(account, broker, params, Lists.newArrayList(price1));
    ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

    // Trigger a sell. Sell on same day at market close.
    when(sellCondition.isMatch(any())).thenReturn(true);
    when(sellOrderParameters.getBarsFromNow()).thenReturn(0);
    when(sellOrderParameters.getMarketTime()).thenReturn(MarketTime.CLOSE);
    when(account.hasShares()).thenReturn(true, false); // Will have no shares after sell.

    simulation.play();

    // Should submit sell order to broker.
    verify(broker).submitOrder(orderCaptor.capture());

    // Verify sell order details.
    SellOrder sellOrder = (SellOrder) orderCaptor.getValue();
    assertThat(sellOrder).isEqualTo(Order.sell()
        .at(MarketTime.CLOSE)
        .barsFromNow(0)
        .date(date1)
        .build());
  }
}

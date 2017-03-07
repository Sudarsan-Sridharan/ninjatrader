package com.bn.ninjatrader.simulation.core;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.entity.PriceBuilderFactory;
import com.bn.ninjatrader.model.util.DummyPriceBuilderFactory;
import com.bn.ninjatrader.simulation.calculator.VarCalculator;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.data.BarDataFactory;
import com.bn.ninjatrader.simulation.model.*;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


/**
 * @author bradwee2000@gmail.com
 */
public class SimulationTest {
  private static final Logger LOG = LoggerFactory.getLogger(SimulationTest.class);

  private final LocalDate date1 = LocalDate.of(2016, 2, 1);
  private final LocalDate date2 = LocalDate.of(2016, 2, 2);
  private final PriceBuilderFactory pbf = new DummyPriceBuilderFactory();
  private final Price price1 = pbf.builder().date(date1).open(1.2).high(1.4).low(1.1).close(1.3).volume(100000).build();
  private final Price price2 = pbf.builder().date(date1).open(2.2).high(2.4).low(2.1).close(2.3).volume(200000).build();
  private final List<Price> prices = Lists.newArrayList(price1, price2);
  private final BarData bar1 = BarData.builder().price(price1).index(0).build();
  private final BarData bar2 = BarData.builder().price(price2).index(1).build();

  private final SimTradeAlgorithm algorithm = SimTradeAlgorithm.builder().build();
  private final SimulationParams params = SimulationParams.builder()
      .startingCash(100000).from(date1).to(date2).symbol("MEG").algorithm(algorithm).build();

  private Account account;
  private Portfolio portfolio;
  private Bookkeeper bookkeeper;
  private Broker broker;
  private BarDataFactory barDataFactory;
  private History history;
  private VarCalculator varCalculator;
  private World world;

  private Simulation simulation;

  @Before
  public void before() {
    account = mock(Account.class);
    portfolio = mock(Portfolio.class);
    bookkeeper = mock(Bookkeeper.class);
    broker = mock(Broker.class);
    barDataFactory = mock(BarDataFactory.class);
    history = mock(History.class);
    varCalculator = mock(VarCalculator.class);

    when(account.getBookkeeper()).thenReturn(bookkeeper);
    when(account.getPortfolio()).thenReturn(portfolio);
    when(account.getLiquidCash()).thenReturn(100000d);
    when(portfolio.isEmpty()).thenReturn(true);
    when(barDataFactory.create(anyString(), any(Price.class), anyInt(), any(World.class), anyList()))
        .thenReturn(bar1, bar2);

    world = World.builder().account(account).broker(broker).pricesForSymbol("MEG", prices).history(history).build();

    simulation = new Simulation(world, params, barDataFactory);
    simulation.addVarCalculators(Lists.newArrayList(varCalculator));
  }

  @Test
  public void testPlay_shouldCreateBarDataForEachPrice() {
    simulation.play();

    // Should forSymbol bar data twice. One for each price.
    verify(barDataFactory).create("MEG", price1, 0,  world, Lists.newArrayList(varCalculator));
    verify(barDataFactory).create("MEG", price2, 1, world, Lists.newArrayList(varCalculator));
  }

  @Test
  public void testPlay_shouldSendBarDataToBrokerForEachPrice() {
    final ArgumentCaptor<BarData> barDataCaptor = ArgumentCaptor.forClass(BarData.class);

    simulation.play();

    // Verify barData is sent to broker for each price
    verify(broker, times(2)).processPendingOrders(barDataCaptor.capture());

    // Verify BarData values
    final List<BarData> barDataList = barDataCaptor.getAllValues();
    assertThat(barDataList.get(0).getPrice()).isEqualTo(price1);
    assertThat(barDataList.get(1).getPrice()).isEqualTo(price2);
  }

  @Test
  public void testPlay_shouldAddBarDataToHistoryForEachPrice() {
    final ArgumentCaptor<BarData> barDataCaptor = ArgumentCaptor.forClass(BarData.class);

    simulation.play();

    // Verify barData is added to history for each price
    verify(history, times(2)).add(barDataCaptor.capture());

    // Verify BarData values
    final List<BarData> barDataList = barDataCaptor.getAllValues();
    assertThat(barDataList.get(0).getPrice()).isEqualTo(price1);
    assertThat(barDataList.get(1).getPrice()).isEqualTo(price2);
  }
}

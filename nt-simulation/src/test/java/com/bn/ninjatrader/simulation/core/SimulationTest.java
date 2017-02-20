package com.bn.ninjatrader.simulation.core;

import com.bn.ninjatrader.common.data.Price;
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
  private final Price price1 = new Price(date1, 1.2, 1.4, 1.1, 1.3, 100000);
  private final Price price2 = new Price(date1, 2.2, 2.4, 2.1, 2.3, 200000);
  private final List<Price> prices = Lists.newArrayList(price1, price2);
  private final BarData bar1 = BarData.builder().price(price1).index(0).build();
  private final BarData bar2 = BarData.builder().price(price2).index(1).build();

  private final SimulationParams params = SimulationParams.builder()
      .startingCash(100000).from(date1).to(date2).symbol("MEG").build();

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

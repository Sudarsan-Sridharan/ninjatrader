package com.bn.ninjatrader.simulation.core;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.account.Account;
import com.bn.ninjatrader.simulation.broker.Broker;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.transaction.Bookkeeper;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


/**
 * @author bradwee2000@gmail.com
 */
public class SimulationTest {
  private static final Logger LOG = LoggerFactory.getLogger(SimulationTest.class);

  private Account account;
  private Bookkeeper bookkeeper;
  private Broker broker;

  private LocalDate date1 = LocalDate.of(2016, 2, 1);
  private LocalDate date2 = LocalDate.of(2016, 2, 2);
  private Price price1 = new Price(date1, 1.2, 1.4, 1.1, 1.3, 100000);
  private Price price2 = new Price(date1, 2.2, 2.4, 2.1, 2.3, 200000);
  private List<Price> prices = Lists.newArrayList(price1, price2);

  private SimulationParams params = SimulationParams.builder()
      .startingCash(100000).from(date1).to(date2).symbol("MEG").build();

  @BeforeMethod
  public void before() {
    account = mock(Account.class);
    bookkeeper = mock(Bookkeeper.class);
    broker = mock(Broker.class);

    when(account.getBookkeeper()).thenReturn(bookkeeper);
    when(account.getCash()).thenReturn(100000d);
  }

  @Test
  public void testPlay_shouldCreateBarData() {
    final ArgumentCaptor<BarData> barDataCaptor = ArgumentCaptor.forClass(BarData.class);

    final Simulation simulation = new Simulation(account, broker, params, prices);
    simulation.play();

    // Should create bar data twice. One for each price.
    verify(broker, times(2)).processPendingOrders(barDataCaptor.capture());
    final List<BarData> barDataList = barDataCaptor.getAllValues();
    assertThat(barDataList.get(0).getPrice()).isEqualTo(price1);
    assertThat(barDataList.get(1).getPrice()).isEqualTo(price2);
  }
}

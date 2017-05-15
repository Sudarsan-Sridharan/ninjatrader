package com.bn.ninjatrader.simulation.core;

import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.simulation.binding.BindingFactory;
import com.bn.ninjatrader.simulation.model.Broker;
import com.bn.ninjatrader.simulation.model.SimContext;
import com.bn.ninjatrader.simulation.model.SimContextFactory;
import com.bn.ninjatrader.simulation.algorithm.AlgorithmScript;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import static com.bn.ninjatrader.model.util.TestUtil.randomPrices;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


/**
 * @author bradwee2000@gmail.com
 */
public class SimulationFactoryTest {
  private static final Logger LOG = LoggerFactory.getLogger(SimulationFactoryTest.class);
  private static final int NUM_OF_PRICES = 5;

  private final LocalDate from = LocalDate.of(2016, 2, 1);
  private final LocalDate to = LocalDate.of(2016, 2, 5);

  private SimContextFactory simContextFactory;
  private PriceDao priceDao;
  private Broker broker;
  private BindingFactory varCalculatorFactory;
  private SimulationRequest simRequest;
  private Clock clock;
  private SimContext context;

  @Before
  public void before() {
    priceDao = mock(PriceDao.class);
    broker = mock(Broker.class);
    varCalculatorFactory = mock(BindingFactory.class);
    simContextFactory = mock(SimContextFactory.class);
    clock = Clock.systemUTC();
    context = mock(SimContext.class, RETURNS_DEEP_STUBS);

    simRequest = SimulationRequest.withSymbol("MEG").from(from).to(to)
        .startingCash(100_000)
        .algorithmScript(mock(AlgorithmScript.class));

    when(simContextFactory.create(any())).thenReturn(context);
    when(priceDao.findPrices()).thenReturn(mock(PriceDao.FindPricesOperation.class, RETURNS_SELF));
    when(priceDao.findPrices().now()).thenReturn(randomPrices(NUM_OF_PRICES));
  }

  @Test
  public void testWorld_shouldSetContextProperties() {
    final List<Price> prices = Lists.newArrayList(mock(Price.class));

    when(priceDao.findPrices().now()).thenReturn(prices);

    final SimulationFactory factory =
        new SimulationFactory(varCalculatorFactory, priceDao, simContextFactory, clock);
    final Simulation simulation = factory.create(simRequest);

    assertThat(simulation).isNotNull();
    assertThat(simulation.getSimContext()).isNotNull();
  }
}

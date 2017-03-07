package com.bn.ninjatrader.simulation.core;

import com.bn.ninjatrader.common.boardlot.BoardLotTable;
import com.bn.ninjatrader.logical.expression.condition.Conditions;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.request.FindPriceRequest;
import com.bn.ninjatrader.simulation.calculator.VarCalculatorFactory;
import com.bn.ninjatrader.simulation.data.BarDataFactory;
import com.bn.ninjatrader.simulation.logicexpression.statement.BuyOrderStatement;
import com.bn.ninjatrader.simulation.logicexpression.statement.ConditionalStatement;
import com.bn.ninjatrader.simulation.logicexpression.statement.MultiStatement;
import com.bn.ninjatrader.simulation.logicexpression.statement.SellOrderStatement;
import com.bn.ninjatrader.simulation.model.Broker;
import com.bn.ninjatrader.simulation.model.BrokerFactory;
import com.bn.ninjatrader.simulation.model.SimTradeAlgorithm;
import com.bn.ninjatrader.simulation.model.World;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

import static com.bn.ninjatrader.model.util.TestUtil.randomPrices;
import static com.bn.ninjatrader.simulation.logicexpression.Variables.PRICE_CLOSE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * @author bradwee2000@gmail.com
 */
public class SimulationFactoryTest {
  private static final Logger LOG = LoggerFactory.getLogger(SimulationFactoryTest.class);
  private static final int NUM_OF_PRICES = 5;

  private final LocalDate from = LocalDate.of(2016, 2, 1);
  private final LocalDate to = LocalDate.of(2016, 2, 5);

  private PriceDao priceDao;
  private BrokerFactory brokerFactory;
  private Broker broker;
  private VarCalculatorFactory varCalculatorFactory;
  private SimulationParams params;
  private BarDataFactory barDataFactory;
  private BoardLotTable boardLotTable;

  @Before
  public void before() {
    priceDao = mock(PriceDao.class);
    brokerFactory = mock(BrokerFactory.class);
    broker = mock(Broker.class);
    barDataFactory = mock(BarDataFactory.class);
    boardLotTable = mock(BoardLotTable.class);
    varCalculatorFactory = mock(VarCalculatorFactory.class);

    params = SimulationParams.builder().symbol("MEG").from(from).to(to)
        .startingCash(100000)
        .algorithm(SimTradeAlgorithm.builder().play(MultiStatement.of(
            ConditionalStatement.builder()
                .condition(Conditions.gt(PRICE_CLOSE, 30))
                .then(BuyOrderStatement.builder().build()).build(),
            ConditionalStatement.builder()
                .condition(Conditions.gt(PRICE_CLOSE, 50))
                .then(SellOrderStatement.builder().build()).build()))
            .build())
        .build();

    when(priceDao.find(any())).thenReturn(randomPrices(NUM_OF_PRICES));
    when(brokerFactory.createBroker()).thenReturn(broker);
  }

  @Test
  public void testWorld_shouldSetWorldProperties() {
    final Broker broker = mock(Broker.class);
    final List<Price> prices = Lists.newArrayList(mock(Price.class));

    when(brokerFactory.createBroker()).thenReturn(broker);
    when(priceDao.find(any(FindPriceRequest.class))).thenReturn(prices);

    final SimulationFactory factory =
        new SimulationFactory(varCalculatorFactory, priceDao, brokerFactory, barDataFactory, boardLotTable);
    final Simulation simulation = factory.create(params);

    assertThat(simulation).isNotNull();

    final World world = simulation.getWorld();

    assertThat(world).isNotNull();
    assertThat(world.getAccount()).isNotNull();
    assertThat(world.getBroker()).isEqualTo(broker);
    assertThat(world.getBoardLotTable()).isEqualTo(boardLotTable);
    assertThat(world.getPrices()).containsOnlyKeys("MEG").containsValue(prices);
    assertThat(world.getHistory()).isNotNull();
    assertThat(world.getProperties()).isNotNull();
    assertThat(world.getChartMarks()).isNotNull();
  }
}

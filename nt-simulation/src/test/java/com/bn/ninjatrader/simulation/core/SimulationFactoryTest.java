package com.bn.ninjatrader.simulation.core;

import com.bn.ninjatrader.logical.expression.condition.Conditions;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.simulation.broker.BrokerFactory;
import com.bn.ninjatrader.simulation.data.BarDataFactory;
import com.bn.ninjatrader.simulation.data.provider.DataProvider;
import com.bn.ninjatrader.simulation.model.DataType;
import com.bn.ninjatrader.simulation.statement.BuyOrderStatement;
import com.bn.ninjatrader.simulation.statement.ConditionalStatement;
import com.bn.ninjatrader.simulation.statement.SellOrderStatement;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

import static com.bn.ninjatrader.common.util.TestUtil.randomPrices;
import static com.bn.ninjatrader.simulation.operation.Variables.PRICE_CLOSE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


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
  private DataProvider dataProvider;
  private List<DataProvider> dataFinderList;
  private SimulationParams params;
  private BarDataFactory barDataFactory;

  @Before
  public void before() {
    priceDao = mock(PriceDao.class);
    brokerFactory = mock(BrokerFactory.class);
    dataProvider = mock(DataProvider.class);
    barDataFactory = mock(BarDataFactory.class);
    dataFinderList = Lists.newArrayList(dataProvider);

    params = SimulationParams.builder().symbol("MEG").from(from).to(to)
        .startingCash(100000)
        .addStatement(ConditionalStatement.builder()
            .condition(Conditions.gt(PRICE_CLOSE, 30))
            .then(BuyOrderStatement.builder().build()).build())
        .addStatement(ConditionalStatement.builder()
            .condition(Conditions.gt(PRICE_CLOSE, 50))
            .then(SellOrderStatement.builder().build()).build())
        .build();

    when(priceDao.find(any())).thenReturn(randomPrices(NUM_OF_PRICES));
    when(dataProvider.getSupportedDataTypes()).thenReturn(Lists.newArrayList(DataType.PRICE_CLOSE));
  }

  @Test
  public void testCreateWithMatchingDataProvider_shouldAddDataProvidersData() {
    final SimulationFactory factory = new SimulationFactory(dataFinderList, priceDao, brokerFactory, barDataFactory);
    factory.create(params);

    // Verify dataProvider data is added to simulation.
    verify(dataProvider).find(params, NUM_OF_PRICES);
  }

  @Test
  public void testCreateWithNoMatchingDataFinder_shouldNotAddDataFindersData() {
    // DataProvider supports different DataType.
    when(dataProvider.getSupportedDataTypes()).thenReturn(Lists.newArrayList(DataType.PRICE_HIGH));

    final SimulationFactory factory = new SimulationFactory(dataFinderList, priceDao, brokerFactory, barDataFactory);
    factory.create(params);

    // Verify dataProvider data not added to simulation.
    verify(dataProvider, times(0)).find(any(), anyInt());
  }
}

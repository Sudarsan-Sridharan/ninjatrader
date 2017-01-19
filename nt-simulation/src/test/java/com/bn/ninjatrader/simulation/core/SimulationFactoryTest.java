package com.bn.ninjatrader.simulation.core;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.simulation.broker.BrokerFactory;
import com.bn.ninjatrader.simulation.condition.Conditions;
import com.bn.ninjatrader.simulation.data.DataType;
import com.bn.ninjatrader.simulation.data.provider.DataProvider;
import com.bn.ninjatrader.simulation.statement.BuyOrderStatement;
import com.bn.ninjatrader.simulation.statement.ConditionalStatment;
import com.bn.ninjatrader.simulation.statement.SellOrderStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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

  @BeforeMethod
  public void before() {
    priceDao = mock(PriceDao.class);
    brokerFactory = mock(BrokerFactory.class);
    dataProvider = mock(DataProvider.class);
    dataFinderList = Lists.newArrayList(dataProvider);

    params = SimulationParams.builder().symbol("MEG").from(from).to(to)
        .startingCash(100000)
        .addStatement(ConditionalStatment.builder()
            .condition(Conditions.gt(PRICE_CLOSE, 30))
            .then(BuyOrderStatement.builder().build()).build())
        .addStatement(ConditionalStatment.builder()
            .condition(Conditions.gt(PRICE_CLOSE, 50))
            .then(SellOrderStatement.builder().build()).build())
        .build();

    when(priceDao.find(any())).thenReturn(randomPrices(NUM_OF_PRICES));
    when(dataProvider.getSupportedDataTypes()).thenReturn(Lists.newArrayList(DataType.PRICE_CLOSE));
  }

  @Test
  public void testCreateWithMatchingDataProvider_shouldAddDataProvidersData() {
    SimulationFactory factory = new SimulationFactory(dataFinderList, priceDao, brokerFactory);
    factory.create(params);

    // Verify dataProvider data is added to simulation.
    verify(dataProvider).find(params, NUM_OF_PRICES);
  }

  @Test
  public void testCreateWithNoMatchingDataFinder_shouldNotAddDataFindersData() {
    // DataProvider supports different DataType.
    when(dataProvider.getSupportedDataTypes()).thenReturn(Lists.newArrayList(DataType.PRICE_HIGH));

    SimulationFactory factory = new SimulationFactory(dataFinderList, priceDao, brokerFactory);
    factory.create(params);

    // Verify dataProvider data not added to simulation.
    verify(dataProvider, times(0)).find(any(), anyInt());
  }
}

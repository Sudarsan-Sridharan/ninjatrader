package com.bn.ninjatrader.simulation.core;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.simulation.broker.BrokerFactory;
import com.bn.ninjatrader.simulation.condition.Conditions;
import com.bn.ninjatrader.simulation.data.DataType;
import com.bn.ninjatrader.simulation.datafinder.DataFinder;
import com.bn.ninjatrader.simulation.order.MarketTime;
import com.bn.ninjatrader.simulation.order.OrderParameters;
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
  private DataFinder dataFinder;
  private List<DataFinder> dataFinderList;
  private SimulationParams params;

  @BeforeMethod
  public void before() {
    priceDao = mock(PriceDao.class);
    brokerFactory = mock(BrokerFactory.class);
    dataFinder = mock(DataFinder.class);
    dataFinderList = Lists.newArrayList(dataFinder);

    params = new SimulationParams();
    params.setSymbol("MEG");
    params.setFromDate(from);
    params.setToDate(to);
    params.setBuyCondition(Conditions.gt(PRICE_CLOSE, 30));
    params.setSellCondition(Conditions.gt(PRICE_CLOSE, 50));
    params.setBuyOrderParams(OrderParameters.buy().at(MarketTime.CLOSE).build());
    params.setSellOrderParams(OrderParameters.sell().at(MarketTime.CLOSE).build());
    params.setStartingCash(100000);

    when(priceDao.find(any())).thenReturn(randomPrices(NUM_OF_PRICES));
    when(dataFinder.getSupportedDataTypes()).thenReturn(Lists.newArrayList(DataType.PRICE_CLOSE));
  }

  @Test
  public void testCreateWithMatchingDataFinder_shouldAddDataFindersData() {
    SimulationFactory factory = new SimulationFactory(dataFinderList, priceDao, brokerFactory);
    factory.create(params);

    // Verify dataFinder data is added to simulation.
    verify(dataFinder).find(params, NUM_OF_PRICES);
  }

  @Test
  public void testCreateWithNoMatchingDataFinder_shouldNotAddDataFindersData() {
    // DataFinder supports different DataType.
    when(dataFinder.getSupportedDataTypes()).thenReturn(Lists.newArrayList(DataType.PRICE_HIGH));

    SimulationFactory factory = new SimulationFactory(dataFinderList, priceDao, brokerFactory);
    factory.create(params);

    // Verify dataFinder data not added to simulation.
    verify(dataFinder, times(0)).find(any(), anyInt());
  }
}

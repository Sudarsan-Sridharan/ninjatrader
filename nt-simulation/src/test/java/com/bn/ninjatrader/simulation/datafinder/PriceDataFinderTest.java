package com.bn.ninjatrader.simulation.datafinder;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.simulation.adaptor.PriceDataMapAdaptor;
import com.bn.ninjatrader.simulation.core.SimulationParams;
import com.bn.ninjatrader.simulation.data.DataMap;
import com.bn.ninjatrader.simulation.data.SimulationData;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;

import static com.bn.ninjatrader.simulation.operation.Variables.PRICE_CLOSE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Brad on 9/1/16.
 */
public class PriceDataFinderTest {

  private final LocalDate fromDate = LocalDate.of(2016, 2, 2);
  private final LocalDate toDate = LocalDate.of(2016, 4, 30);
  private final Price price1 = TestUtil.randomPrice();

  private PriceDao priceDao;
  private PriceDataMapAdaptor dataMapAdaptor;
  private SimulationParams params;
  private DataMap dataMap;
  private PriceDataFinder dataFinder;

  @BeforeMethod
  public void setup() {
    priceDao = mock(PriceDao.class);
    dataMapAdaptor = mock(PriceDataMapAdaptor.class);
    dataFinder = new PriceDataFinder(priceDao, dataMapAdaptor);

    params = new SimulationParams();
    params.setSymbol("MEG");
    params.setFromDate(fromDate);
    params.setToDate(toDate);

    dataMap = new DataMap();
    dataMap.put(PRICE_CLOSE, price1.getClose());

    when(priceDao.find(any())).thenReturn(Lists.newArrayList(price1));
    when(dataMapAdaptor.toDataMap(price1)).thenReturn(dataMap);
  }

  @Test
  public void testFind_shouldReturnSimulationData() {
    final List<SimulationData<Price>> dataList = dataFinder.find(params, 100);
    assertThat(dataList).isNotNull().hasSize(1);

    // Should return SimulationData.
    final SimulationData<Price> simulationData = dataList.get(0);
    assertThat(simulationData.size()).isEqualTo(1);

    // Verify variable values are correct.
    final DataMap dataMap = simulationData.getDataMap(0);
    assertThat(dataMap).isNotNull().hasSize(1);
    assertThat(dataMap.get(PRICE_CLOSE)).isEqualTo(price1.getClose());
  }
}

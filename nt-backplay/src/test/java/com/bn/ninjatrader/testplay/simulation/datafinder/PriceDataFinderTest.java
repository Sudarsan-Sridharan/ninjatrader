package com.bn.ninjatrader.testplay.simulation.datafinder;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.period.FindRequest;
import com.bn.ninjatrader.testplay.simulation.SimulationParams;
import com.bn.ninjatrader.testplay.simulation.adaptor.PriceDataMapAdaptor;
import com.bn.ninjatrader.testplay.simulation.data.DataMap;
import com.bn.ninjatrader.testplay.simulation.data.DataType;
import com.bn.ninjatrader.testplay.simulation.data.SimulationData;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created by Brad on 9/1/16.
 */
public class PriceDataFinderTest {

  @Tested
  private PriceDataFinder dataFinder;

  @Injectable
  private PriceDao priceDao;

  @Injectable
  private PriceDataMapAdaptor dataMapAdaptor;

  private final LocalDate fromDate = LocalDate.of(2016, 2, 2);
  private final LocalDate toDate = LocalDate.of(2016, 4, 30);

  private final Price price1 = TestUtil.randomPrice();

  private SimulationParams params;
  private DataMap dataMap;

  @BeforeMethod
  public void setup() {
    params = new SimulationParams();
    params.setSymbol("MEG");
    params.setFromDate(fromDate);
    params.setToDate(toDate);

    dataMap = new DataMap();
    dataMap.put(DataType.PRICE_CLOSE, price1.getClose());

    new Expectations() {{
      priceDao.find(withInstanceOf(FindRequest.class));
      result = Lists.newArrayList(price1);

      dataMapAdaptor.toDataMap(price1);
      result = dataMap;
    }};
  }

  @Test
  public void testFind() {
    List<SimulationData<Price>> dataList = dataFinder.find(params, 100);
    assertNotNull(dataList);
    assertEquals(dataList.size(), 1);

    SimulationData<Price> simulationData = dataList.get(0);
    assertEquals(simulationData.size(), 1);

    DataMap dataMap = simulationData.getDataMap(0);
    assertNotNull(dataMap);
    assertEquals(dataMap.size(), 1);
    assertEquals(dataMap.get(DataType.PRICE_CLOSE), price1.getClose());
  }
}

package com.bn.ninjatrader.testplay.simulation.datafinder;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.model.dao.period.FindRequest;
import com.bn.ninjatrader.service.indicator.IchimokuService;
import com.bn.ninjatrader.testplay.simulation.SimulationParams;
import com.bn.ninjatrader.testplay.simulation.adaptor.IchimokuDataMapAdaptor;
import com.bn.ninjatrader.testplay.simulation.data.DataMap;
import com.bn.ninjatrader.testplay.simulation.data.SimulationData;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;

import static com.bn.ninjatrader.testplay.simulation.data.DataType.KIJUN;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created by Brad on 9/1/16.
 */
public class IchimokuDataFinderTest {

  @Tested
  private IchimokuDataFinder dataFinder;

  @Injectable
  private IchimokuService ichimokuService;

  @Injectable
  private IchimokuDataMapAdaptor dataMapAdaptor;

  private final LocalDate fromDate = LocalDate.of(2016, 2, 2);
  private final LocalDate toDate = LocalDate.of(2016, 4, 30);

  private final Ichimoku ichimoku = new Ichimoku(fromDate, 1, 2, 3, 4, 5);

  private SimulationParams params;
  private DataMap dataMap;

  @BeforeMethod
  public void setup() {
    params = new SimulationParams();
    params.setSymbol("MEG");
    params.setFromDate(fromDate);
    params.setToDate(toDate);

    dataMap = new DataMap();
    dataMap.put(KIJUN, ichimoku.getKijun());

    new Expectations() {{
      ichimokuService.find(withInstanceOf(FindRequest.class));
      result = Lists.newArrayList(ichimoku);

      dataMapAdaptor.toDataMap(ichimoku);
      result = dataMap;
    }};
  }

  @Test
  public void testFind() {
    final int requiredSize = 10;
    List<SimulationData<Ichimoku>> dataList = dataFinder.find(params, requiredSize);
    assertNotNull(dataList);
    assertEquals(dataList.size(), 1);

    SimulationData<Ichimoku> simulationData = dataList.get(0);
    assertEquals(simulationData.size(), requiredSize);

    DataMap dataMap = simulationData.getDataMap(requiredSize - 1);
    assertNotNull(dataMap);
    assertEquals(dataMap.size(), 1);
    assertEquals(dataMap.get(KIJUN), ichimoku.getKijun());
  }
}

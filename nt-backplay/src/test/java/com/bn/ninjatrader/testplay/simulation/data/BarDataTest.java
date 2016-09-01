package com.bn.ninjatrader.testplay.simulation.data;

import org.testng.annotations.Test;

import static com.bn.ninjatrader.testplay.simulation.data.DataType.*;
import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 8/11/16.
 */
public class BarDataTest {

  @Test
  public void testPutDataMap() {
    DataMap dataMap = new DataMap();
    dataMap.put(CHIKOU, 100d);
    dataMap.put(TENKAN, 200d);

    BarData params = new BarData().put(dataMap);

    assertEquals(params.get(CHIKOU), 100d);
    assertEquals(params.get(TENKAN), 200d);
  }

  @Test
  public void testOverwriteDataMap() {
    DataMap dataMap = new DataMap();
    dataMap.put(CHIKOU, 100d);
    dataMap.put(TENKAN, 200d);

    BarData params = new BarData().put(dataMap);

    DataMap overlayDataMap = new DataMap();
    overlayDataMap.put(TENKAN, 300d);
    overlayDataMap.put(KIJUN, 400d);

    params.put(overlayDataMap);

    assertEquals(params.get(CHIKOU), 100d);
    assertEquals(params.get(TENKAN), 300d);
    assertEquals(params.get(KIJUN), 400d);
  }
}

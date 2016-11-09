package com.bn.ninjatrader.simulation.data;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.simulation.adaptor.DataMapAdaptor;
import org.testng.annotations.Test;

import java.util.List;

import static com.bn.ninjatrader.simulation.data.DataType.PRICE_CLOSE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created by Brad on 8/18/16.
 */
public class SimulationDataTest {

  private DataMapAdaptor<Double> adaptor = new DataMapAdaptor<Double>() {
    @Override
    public DataMap toDataMap(Double value) {
      DataMap dataMap = new DataMap();
      dataMap.put(PRICE_CLOSE, value);
      return dataMap;
    }
  };

  @Test
  public void testGetDataMap() {
    List<Double> values = Lists.newArrayList(1d, 2d, 3d);

    SimulationData<Double> data = new SimulationData<>(values, adaptor);
    assertEquals(data.size(), 3);

    assertNotNull(data.getDataMap(0));
    assertEquals(data.getDataMap(0).get(PRICE_CLOSE), 1d);
    assertEquals(data.getDataMap(1).get(PRICE_CLOSE), 2d);
    assertEquals(data.getDataMap(2).get(PRICE_CLOSE), 3d);
  }
}

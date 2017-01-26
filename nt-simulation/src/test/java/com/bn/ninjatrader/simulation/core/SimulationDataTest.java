package com.bn.ninjatrader.simulation.core;

import com.bn.ninjatrader.simulation.adaptor.DataMapAdaptor;
import com.bn.ninjatrader.simulation.data.DataMap;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static com.bn.ninjatrader.simulation.operation.Variables.PRICE_CLOSE;
import static org.assertj.core.api.Assertions.assertThat;

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
    final List<Double> values = Lists.newArrayList(1d, 2d, 3d);
    final SimulationData<Double> data = new SimulationData<>(values, adaptor);
    assertThat(data.size()).isEqualTo(3);
    assertThat(data.getDataAtIndex(0)).isNotNull();
    assertThat(data.getDataAtIndex(0).get(PRICE_CLOSE)).isEqualTo(1d);
    assertThat(data.getDataAtIndex(1).get(PRICE_CLOSE)).isEqualTo(2d);
    assertThat(data.getDataAtIndex(2).get(PRICE_CLOSE)).isEqualTo(3d);
  }
}

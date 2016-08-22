package com.bn.ninjatrader.testplay.simulation.data.adaptor;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.testplay.simulation.data.DataMap;
import com.bn.ninjatrader.common.data.DataType;

/**
 * Created by Brad on 8/17/16.
 */
public class SimpleAverageDataMapAdaptor implements DataMapAdaptor<Value> {

  private static final String DATA_TYPE_PREFIX = "SMA_";

  private final int period;

  public static final SimpleAverageDataMapAdaptor forPeriod(int period) {
    return new SimpleAverageDataMapAdaptor(period);
  }

  private SimpleAverageDataMapAdaptor(int period) {
    this.period = period;
  }

  @Override
  public DataMap toDataMap(Value value) {
    DataMap dataMap = new DataMap();
    DataType dataType = DataType.valueOf(DATA_TYPE_PREFIX + period);

    dataMap.put(dataType, value.getValue());
    return dataMap;
  }
}

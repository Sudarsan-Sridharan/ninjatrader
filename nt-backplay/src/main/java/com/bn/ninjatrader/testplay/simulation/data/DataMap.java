package com.bn.ninjatrader.testplay.simulation.data;

import com.bn.ninjatrader.common.data.DataType;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by Brad on 8/18/16.
 */
public class DataMap {

  private static final String DATA_TYPE_NOT_EXIST_ERROR = "Value does not exist for the DataType: %s";

  private Map<DataType, Double> dataMap = Maps.newHashMap();

  public void put(DataType dataType, double value) {
    dataMap.put(dataType, value);
  }

  public void put(Map<DataType, Double> dataMap) {
    this.dataMap.putAll(dataMap);
  }

  public void put(DataMap dataMap) {
    this.dataMap.putAll(dataMap.dataMap);
  }

  public Double get(DataType dataType) {
    if (dataMap.containsKey(dataType)) {
      return dataMap.get(dataType);
    } else {
      throw new IllegalArgumentException(String.format(DATA_TYPE_NOT_EXIST_ERROR, dataType));
    }
  }

  public int size() {
    return dataMap.size();
  }

  public Map<DataType, Double> toMap() {
    return dataMap;
  }

  public void clear() {
    dataMap.clear();
  }
}

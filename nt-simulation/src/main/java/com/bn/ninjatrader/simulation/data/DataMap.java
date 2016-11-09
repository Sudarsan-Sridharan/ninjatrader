package com.bn.ninjatrader.simulation.data;

import com.bn.ninjatrader.simulation.operation.Variable;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by Brad on 8/18/16.
 */
public class DataMap {

  private static final Logger LOG = LoggerFactory.getLogger(DataMap.class);

  private static final String DATA_TYPE_NOT_EXIST_ERROR = "Value does not exist for the DataType: %s";

  private Map<Variable, Double> dataMap = Maps.newHashMap();

  public void put(Variable variable, double value) {
    dataMap.put(variable, value);
  }

  public void put(DataType dataType, double value) {
    put(Variable.of(dataType), value);
  }

  public void put(Map<Variable, Double> dataMap) {
    this.dataMap.putAll(dataMap);
  }

  public void put(DataMap dataMap) {
    this.dataMap.putAll(dataMap.dataMap);
  }

  public Double get(Variable variable) {
    if (dataMap.containsKey(variable)) {
      return dataMap.get(variable);
    } else {
      LOG.info("{}", dataMap.keySet());
      throw new IllegalArgumentException(String.format(DATA_TYPE_NOT_EXIST_ERROR, variable));
    }
  }

  public Double get(DataType dataType) {
    Variable variable = Variable.of(dataType);
    return get(variable);
  }

  public int size() {
    return dataMap.size();
  }

  public Map<Variable, Double> toMap() {
    return dataMap;
  }

  public void clear() {
    dataMap.clear();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("dataMap", dataMap)
        .build();
  }
}

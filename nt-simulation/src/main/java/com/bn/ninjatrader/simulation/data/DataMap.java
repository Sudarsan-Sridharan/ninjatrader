package com.bn.ninjatrader.simulation.data;

import com.bn.ninjatrader.simulation.operation.Variable;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by Brad on 8/18/16.
 */
public class DataMap implements Map<Variable, Double> {

  private static final Logger LOG = LoggerFactory.getLogger(DataMap.class);

  private static final String DATA_TYPE_NOT_EXIST_ERROR = "Value does not exist for the DataType: %s";

  private Map<Variable, Double> variableMap = Maps.newHashMap();

  @Override
  public Double put(Variable key, Double value) {
    return variableMap.put(key, value);
  }

  public Double put(Variable key, double value) {
    return put(key, Double.valueOf(value));
  }

  @Override
  public void putAll(Map<? extends Variable, ? extends Double> dataMap) {
    this.variableMap.putAll(dataMap);
  }

  @Override
  public Double get(Object key) {
    if (variableMap.containsKey(key)) {
      return variableMap.get(key);
    } else {
      LOG.info("{}", variableMap.keySet());
      throw new IllegalArgumentException(String.format(DATA_TYPE_NOT_EXIST_ERROR, key));
    }
  }

  @Override
  public int size() {
    return variableMap.size();
  }

  @Override
  public boolean isEmpty() {
    return variableMap.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return variableMap.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return variableMap.containsValue(value);
  }

  @Override
  public Double remove(Object key) {
    return variableMap.remove(key);
  }

  public Map<Variable, Double> toMap() {
    return variableMap;
  }

  @Override
  public void clear() {
    variableMap.clear();
  }

  @Override
  public Set<Variable> keySet() {
    return variableMap.keySet();
  }

  @Override
  public Collection<Double> values() {
    return variableMap.values();
  }

  @Override
  public Set<Entry<Variable, Double>> entrySet() {
    return variableMap.entrySet();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("variableMap", variableMap)
        .build();
  }
}

package com.bn.ninjatrader.simulation.data;

import com.bn.ninjatrader.simulation.logic.Variable;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by Brad on 8/18/16.
 */
public class DataMap implements Map<Variable, Object> {
  private static final Logger LOG = LoggerFactory.getLogger(DataMap.class);
  private static final String DATA_TYPE_NOT_EXIST_ERROR = "Value does not exist for the DataType: %s";

  // TODO make unmodifiable!
  private static final DataMap EMPTY_INSTANCE = DataMap.newInstance();

  public static DataMap empty() {
    return EMPTY_INSTANCE;
  }
  public static final DataMap newInstance() {
    return new DataMap();
  }

  private Map<Variable, Object> variableMap = Maps.newHashMap();

  private Map<String, Object> bindings = Maps.newHashMap();

  public DataMap addData(final Variable key, Object value) {
    variableMap.put(key, value);
    bindings.put(key.getName(), value);
    return this;
  }

  @Override
  public Object put(Variable key, Object value) {
    bindings.put(key.getName(), value);
    return variableMap.put(key, value);
  }

  public Object put(Variable key, double value) {
    return put(key, Double.valueOf(value));
  }

  @Override
  public void putAll(Map<? extends Variable, ? extends Object> dataMap) {
    for (Entry<? extends Variable, ? extends Object> e : dataMap.entrySet()) {
      bindings.put(e.getKey().getName(), e.getValue());
    }
    this.variableMap.putAll(dataMap);
  }

  @Override
  public Object get(Object key) {
    if (variableMap.containsKey(key)) {
      return variableMap.get(key);
    } else {
      throw new IllegalArgumentException(String.format(DATA_TYPE_NOT_EXIST_ERROR, key));
    }
  }

  public Object get2(String key) {
    if (bindings.containsKey(key)) {
      return bindings.get(key);
    } else {
      throw new IllegalArgumentException(String.format(DATA_TYPE_NOT_EXIST_ERROR, key));
    }
  }

  public Map<String, Object> getBindings() {
    return bindings;
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
  public Object remove(Object key) {
    return variableMap.remove(key);
  }

  public Map<Variable, Object> toMap() {
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
  public Collection<Object> values() {
    return variableMap.values();
  }

  @Override
  public Set<Entry<Variable, Object>> entrySet() {
    return variableMap.entrySet();
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("variableMap", variableMap).toString();
  }
}

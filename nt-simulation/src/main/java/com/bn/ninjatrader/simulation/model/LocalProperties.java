package com.bn.ninjatrader.simulation.model;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author bradwee2000@gmail.com
 */
public class LocalProperties {

  private final Map<String, Double> doubleProperties = Maps.newHashMap();
  private final Map<String, Boolean> booleanProperties = Maps.newHashMap();

  public LocalProperties put(final String key, final Double value) {
    doubleProperties.put(key, value);
    booleanProperties.remove(key);
    return this;
  }

  public LocalProperties put(final String key, final boolean value) {
    put(key, value ? Boolean.TRUE : Boolean.FALSE);
    return this;
  }

  public LocalProperties put(final String key, final Boolean value) {
    booleanProperties.put(key, value);
    doubleProperties.remove(key);
    return this;
  }

  public Boolean getBoolean(final String key) {
    return booleanProperties.get(key);
  }

  public Double getDouble(final String key) {
    return doubleProperties.get(key);
  }

  public LocalProperties remove(final String key) {
    doubleProperties.remove(key);
    booleanProperties.remove(key);
    return this;
  }

  public int size() {
    return doubleProperties.size() + booleanProperties.size();
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("doubleProperties", doubleProperties)
        .add("booleanProperties", booleanProperties)
        .toString();
  }
}

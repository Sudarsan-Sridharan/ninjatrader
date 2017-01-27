package com.bn.ninjatrader.simulation.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author bradwee2000@gmail.com
 */
public class LocalProperties implements Map<String, Property> {

  private final Map<String, Property> properties = Maps.newHashMap();

  public LocalProperties put(final String key, final double value) {
    properties.put(key, Property.of(key, value));
    return this;
  }

  public LocalProperties put(final String key, final boolean value) {
    properties.put(key, Property.of(key, value));
    return this;
  }

  public LocalProperties remove(final String key) {
    properties.remove(key);
    return this;
  }

  public int size() {
    return properties.size();
  }

  @Override
  public boolean isEmpty() {
    return properties.isEmpty();
  }

  @Override
  public boolean containsKey(final Object key) {
    return properties.containsKey(key);
  }

  @Override
  public boolean containsValue(final Object value) {
    return properties.containsValue(value);
  }

  @Override
  public Property get(final Object key) {
    return properties.get(key);
  }

  @Override
  public Property put(final String key, final Property value) {
    return properties.put(key, value);
  }

  @Override
  public Property remove(final Object key) {
    return properties.remove(key);
  }

  @Override
  public void putAll(final Map<? extends String, ? extends Property> m) {
    this.properties.putAll(m);
  }

  public LocalProperties putAll(final LocalProperties properties) {
    this.properties.putAll(properties);
    return this;
  }

  @Override
  public void clear() {
    properties.clear();
  }

  @Override
  public Set<String> keySet() {
    return properties.keySet();
  }

  @Override
  public Collection<Property> values() {
    return properties.values();
  }

  @Override
  public Set<Entry<String, Property>> entrySet() {
    return properties.entrySet();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof LocalProperties)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final LocalProperties rhs = (LocalProperties) obj;
    return Objects.equal(properties, rhs.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(properties);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("properties", properties).toString();
  }
}

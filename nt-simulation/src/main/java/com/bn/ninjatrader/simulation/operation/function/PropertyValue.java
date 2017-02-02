package com.bn.ninjatrader.simulation.operation.function;

import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.LocalProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Set;

/**
 * Created by Brad on 8/29/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PropertyValue implements Operation<BarData> {
  private static final Logger LOG = LoggerFactory.getLogger(PropertyValue.class);

  public static final PropertyValue of(final String key) {
    return new PropertyValue(key);
  }

  @JsonProperty("key")
  private final String key;

  public PropertyValue(@JsonProperty("key") final String key) {
    this.key = key;
  }

  @Override
  public double getValue(final BarData barData) {
    final LocalProperties properties = barData.getWorld().getProperties();
    final boolean isContainsKey = properties.containsKey(key);
    return isContainsKey ? properties.get(key).getValue() : Double.NaN;
  }

  @Override
  public Set<Variable> getVariables() {
    return Collections.emptySet();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof PropertyValue)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final PropertyValue rhs = (PropertyValue) obj;
    return Objects.equal(key, rhs.key);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(key);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("key", key).toString();
  }
}

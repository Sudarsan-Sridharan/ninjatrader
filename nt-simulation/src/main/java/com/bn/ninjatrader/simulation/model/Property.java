package com.bn.ninjatrader.simulation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Property {

  public static final Property of(final String key, final double value) {
    return new Property(key, value);
  }

  public static final Property of(final String key, final boolean value) {
    return new Property(key, value ? Double.POSITIVE_INFINITY : Double.NaN);
  }

  @JsonProperty("key")
  private final String key;

  @JsonProperty("value")
  private final double value;

  public Property(@JsonProperty("key") final String key,
                  @JsonProperty("value") final double value) {
    checkNotNull(key, "key must not be null.");
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public double getValue() {
    return value;
  }

  @JsonIgnore
  public boolean getValueAsBoolean() {
    return Double.isNaN(value) ? false : true;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof Property)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final Property rhs = (Property) obj;
    return Objects.equal(key, rhs.key)
        && Objects.equal(value, rhs.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(key, value);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("key", key).add("value", value).toString();
  }
}

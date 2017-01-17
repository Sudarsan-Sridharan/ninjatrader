package com.bn.ninjatrader.common.data;

import com.bn.ninjatrader.common.util.NtLocalDateDeserializer;
import com.bn.ninjatrader.common.util.NtLocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.time.LocalDate;

/**
 * Created by Brad on 5/1/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Value implements DateObj<Value> {
  private static Value EMPTY_INSTANCE = new Value();

  public static Value empty() {
    return EMPTY_INSTANCE;
  }
  public static Value of(LocalDate date, double value) {
    return new Value(date, value);
  }

  @JsonProperty("d")
  @JsonSerialize(using = NtLocalDateSerializer.class)
  @JsonDeserialize(using = NtLocalDateDeserializer.class)
  private LocalDate date;

  @JsonProperty("v")
  private double value;

  public Value() {}

  public Value(LocalDate date, double value) {
    this.date = date;
    this.value = value;
  }

  public LocalDate getDate() {
    return date;
  }

  public double getValue() {
    return value;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(date, value);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof Value)) { return false; }
    if (obj == this) { return true; }
    final Value rhs = (Value) obj;
    return Objects.equal(date, rhs.date)
        && Objects.equal(value, rhs.value);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("D", date).add("V", value).toString();
  }

  public int compareTo(Value v2) {
    return getDate().compareTo(v2.getDate());
  }
}


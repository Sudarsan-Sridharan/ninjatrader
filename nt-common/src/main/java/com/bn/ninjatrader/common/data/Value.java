package com.bn.ninjatrader.common.data;

import com.bn.ninjatrader.common.util.NtLocalDateDeserializer;
import com.bn.ninjatrader.common.util.NtLocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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

  @JsonProperty("d")
  @JsonSerialize(using = NtLocalDateSerializer.class)
  @JsonDeserialize(using = NtLocalDateDeserializer.class)
  private LocalDate date;

  @JsonProperty("v")
  private double value;

  public static Value of(LocalDate date, double value) {
    return new Value(date, value);
  }

  public Value() {}

  public Value(LocalDate date, double value) {
    this.date = date;
    this.value = value;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("D", date)
        .append("V", value)
        .toString();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(date)
        .append(value)
        .toHashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) { return false; }
    if (!(obj instanceof Value)) {
      return false;
    }
    if (obj == this) { return true; }
    if (obj.getClass() != getClass()) {
      return false;
    }

    Value rhs = (Value) obj;
    return date.equals(rhs.getDate()) &&
        value == rhs.getValue();
  }

  public int compareTo(Value v2) {
    return getDate().compareTo(v2.getDate());
  }
}


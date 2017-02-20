package com.bn.ninjatrader.common.data;

import com.bn.ninjatrader.common.translator.LocalDateTranslatorFactory;
import com.bn.ninjatrader.common.util.NtLocalDateDeserializer;
import com.bn.ninjatrader.common.util.NtLocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.googlecode.objectify.annotation.Translate;

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
  @Translate(value = LocalDateTranslatorFactory.class)
  private LocalDate d; // Date. Names are abbreviated to save space in Datastore

  @JsonProperty("v")
  private double v; // Value

  public Value() {}

  public Value(final LocalDate date, final double value) {
    this.d = date;
    this.v = value;
  }

  @JsonIgnore
  public LocalDate getDate() {
    return d;
  }

  @JsonIgnore
  public double getValue() {
    return v;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(d, v);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) { return true; }
    if (obj == null || !(obj instanceof Value)) { return false; }
    final Value rhs = (Value) obj;
    return Objects.equal(d, rhs.d)
        && Objects.equal(v, rhs.v);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("D", d).add("V", v).toString();
  }

  public int compareTo(final Value v2) {
    return getDate().compareTo(v2.getDate());
  }
}


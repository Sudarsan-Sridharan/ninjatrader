package com.bn.ninjatrader.model.deprecated;

import com.bn.ninjatrader.common.util.NtLocalDateDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.time.LocalDate;

/**
 * Created by Brad on 5/1/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RSIValue extends Value {
  private static final RSIValue EMPTY_INSTANCE = new RSIValue(null, 0);

  public static final RSIValue empty() {
    return EMPTY_INSTANCE;
  }
  public static final RSIValue of(final LocalDate date, final double value) {
    return new RSIValue(date, value);
  }
  public static final RSIValue of(final LocalDate date, final double value,
                                  final double avgGain, final double avgLoss) {
    return new RSIValue(date, value, avgGain, avgLoss);
  }

  @JsonProperty("g")
  private final double g; // Average gain. Names are abbreviated to save space in datastore.

  @JsonProperty("l")
  private final double l; // Average loss

  public RSIValue(final LocalDate date, final double value) {
    super(date, value);
    g = 0;
    l = 0;
  }

  public RSIValue(@JsonDeserialize(using = NtLocalDateDeserializer.class)
                  @JsonProperty("d") final LocalDate date,
                  @JsonProperty("v") final double value,
                  @JsonProperty("g") final double avgGain,
                  @JsonProperty("l") final double avgLoss) {
    super(date, value);
    this.g = avgGain;
    this.l = avgLoss;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) { return true; }
    if (obj == null || !(obj instanceof RSIValue)) { return false; }
    final RSIValue rhs = (RSIValue) obj;
    return Objects.equal(getDate(), rhs.getDate())
        && Objects.equal(getValue(), rhs.getValue())
        && Objects.equal(g, rhs.g)
        && Objects.equal(l, rhs.l);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("D", getDate()).add("V", getValue()).add("avgGain", g).add("avgLoss", l).toString();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getDate(), getValue(), g, l);
  }

  public double getAvgGain() {
    return g;
  }

  public double getAvgLoss() {
    return l;
  }
}


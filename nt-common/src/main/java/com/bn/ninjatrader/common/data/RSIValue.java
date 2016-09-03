package com.bn.ninjatrader.common.data;

import com.bn.ninjatrader.common.util.NtLocalDateDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;

/**
 * Created by Brad on 5/1/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RSIValue extends Value {

  private static RSIValue EMPTY_INSTANCE = new RSIValue();

  public static RSIValue empty() {
    return EMPTY_INSTANCE;
  }

  @JsonProperty("g")
  private double avgGain;

  @JsonProperty("l")
  private double avgLoss;

  public RSIValue() {}

  public RSIValue(LocalDate date, double value) {
    super(date, value);
  }

  public RSIValue(@JsonProperty("d") @JsonDeserialize(using = NtLocalDateDeserializer.class) LocalDate date,
                  @JsonProperty("v") double value,
                  @JsonProperty("g") double avgGain,
                  @JsonProperty("l") double avgLoss) {
    super(date, value);
    this.avgGain = avgGain;
    this.avgLoss = avgLoss;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("D", getDate())
        .append("V", getValue())
        .append("avgGain", avgGain)
        .append("avgLoss", avgLoss)
        .toString();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .appendSuper(super.hashCode())
        .append(avgGain)
        .append(avgLoss)
        .toHashCode();
  }

  public double getAvgGain() {
    return avgGain;
  }

  public void setAvgGain(double avgGain) {
    this.avgGain = avgGain;
  }

  public double getAvgLoss() {
    return avgLoss;
  }

  public void setAvgLoss(double avgLoss) {
    this.avgLoss = avgLoss;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) { return false; }
    if (!(obj instanceof RSIValue)) {
      return false;
    }
    if (obj == this) { return true; }
    if (obj.getClass() != getClass()) {
      return false;
    }

    RSIValue rhs = (RSIValue) obj;
    return new EqualsBuilder().appendSuper(super.equals(rhs))
        .append(avgGain, rhs.avgGain)
        .append(avgLoss, rhs.avgLoss)
        .isEquals();
  }
}


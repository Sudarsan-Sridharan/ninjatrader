package com.bn.ninjatrader.common.data;

import com.bn.ninjatrader.common.util.NtLocalDateDeserializer;
import com.bn.ninjatrader.common.util.NtLocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;

/**
 * Created by Brad on 5/1/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Price implements DateObj<Price> {

  @JsonProperty("o")
  private double open;

  @JsonProperty("h")
  private double high;

  @JsonProperty("l")
  private double low;

  @JsonProperty("c")
  private double close;

  @JsonProperty("v")
  private long volume;

  @JsonProperty("d")
  @JsonSerialize(using = NtLocalDateSerializer.class)
  @JsonDeserialize(using = NtLocalDateDeserializer.class)
  private LocalDate date;

  public Price() {}

  public Price(double open, double high, double low, double close, long volume) {
    this(null, open, high, low, close, volume);
  }

  public Price(LocalDate date, double open, double high, double low, double close, long volume) {
    this.open = open;
    this.high = high;
    this.low = low;
    this.close = close;
    this.volume = volume;
    this.date = date;
  }

  public double getOpen() {
    return open;
  }

  public void setOpen(double open) {
    this.open = open;
  }

  public double getHigh() {
    return high;
  }

  public void setHigh(double high) {
    this.high = high;
  }

  public double getLow() {
    return low;
  }

  public void setLow(double low) {
    this.low = low;
  }

  public double getClose() {
    return close;
  }

  public void setClose(double close) {
    this.close = close;
  }

  public long getVolume() {
    return volume;
  }

  public void setVolume(long volume) {
    this.volume = volume;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public void addVolume(long additionalVolume) {
    this.volume += additionalVolume;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) { return false; }
    if (!(obj instanceof Price)) {
      return false;
    }
    if (obj == this) { return true; }
    if (obj.getClass() != getClass()) {
      return false;
    }

    Price rhs = (Price) obj;

    return new EqualsBuilder()
        .append(date, rhs.getDate())
        .append(open, rhs.getOpen())
        .append(high, rhs.getHigh())
        .append(low, rhs.getLow())
        .append(close, rhs.getClose())
        .append(volume, rhs.getVolume())
        .build();
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("D", date)
        .append("O", open)
        .append("H", high)
        .append("L", low)
        .append("C", close)
        .append("V", volume)
        .toString();
  }

  public int compareTo(Price price2) {
    return getDate().compareTo(price2.getDate());
  }
}


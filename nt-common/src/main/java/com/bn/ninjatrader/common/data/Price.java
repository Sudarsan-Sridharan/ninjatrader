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
public class Price implements DateObj<Price> {
  public static final Builder builder() {
    return new Builder();
  }

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

  @JsonProperty("ch")
  private double change;

  @JsonProperty("d")
  @JsonSerialize(using = NtLocalDateSerializer.class)
  @JsonDeserialize(using = NtLocalDateDeserializer.class)
  private LocalDate date;

  public Price(final LocalDate date,
               final double open,
               final double high,
               final double low,
               final double close,
               final long volume) {
    this(date, open, high, low, close, 0d, volume);
  }

  public Price(@JsonProperty("d")
               @JsonSerialize(using = NtLocalDateSerializer.class)
               @JsonDeserialize(using = NtLocalDateDeserializer.class)
               final LocalDate date,
               @JsonProperty("o") final double open,
               @JsonProperty("h") final double high,
               @JsonProperty("l") final double low,
               @JsonProperty("c") final double close,
               @JsonProperty("ch") final double change,
               @JsonProperty("v") final long volume) {
    this.date = date;
    this.open = open;
    this.high = high;
    this.low = low;
    this.close = close;
    this.change = change;
    this.volume = volume;
  }

  public double getOpen() {
    return open;
  }

  public double getHigh() {
    return high;
  }

  public double getLow() {
    return low;
  }

  public double getClose() {
    return close;
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

  public double getChange() {
    return change;
  }

  public void setChange(double change) {
    this.change = change;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || obj.getClass() != getClass()) { return false; }
    if (obj == this) { return true; }
    final Price rhs = (Price) obj;
    return Objects.equal(date, rhs.date)
        && Objects.equal(open, rhs.open)
        && Objects.equal(high, rhs.high)
        && Objects.equal(low, rhs.low)
        && Objects.equal(close, rhs.close)
        && Objects.equal(change, rhs.change)
        && Objects.equal(volume, rhs.volume);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(date, open, high, low, close, change, volume);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("D", date).add("O", open).add("H", high).add("L", low).add("C", close).add("CH", change).add("V", volume)
        .toString();
  }

  public int compareTo(final Price other) {
    return getDate().compareTo(other.getDate());
  }

  /**
   * Builder class
   */
  public static final class Builder {
    private LocalDate date;
    private double open;
    private double high;
    private double low;
    private double close;
    private long volume;
    private double change;

    public Builder copyOf(final Price price) {
      this.date = price.date;
      this.open = price.open;
      this.high = price.high;
      this.low = price.low;
      this.close = price.close;
      this.change = price.change;
      this.volume = price.volume;
      return this;
    }
    public Builder date(final LocalDate date) {
      this.date = date;
      return this;
    }
    public Builder open(final double open) {
      this.open = open;
      return this;
    }
    public Builder high(final double high) {
      this.high = high;
      return this;
    }
    public Builder low(final double low) {
      this.low = low;
      return this;
    }
    public Builder close(final double close) {
      this.close = close;
      return this;
    }
    public Builder change(final double change) {
      this.change = change;
      return this;
    }
    public Builder volume(final long volume) {
      this.volume = volume;
      return this;
    }
    public Builder addVolume(final long volume) {
      this.volume += volume;
      return this;
    }

    public LocalDate getDate() {
      return date;
    }
    public double getOpen() {
      return open;
    }
    public double getHigh() { return high; }
    public double getLow() {
      return low;
    }
    public double getClose() {
      return close;
    }
    public double getChange() {
      return change;
    }
    public long getVolume() {
      return volume;
    }

    public Price build() {
      return new Price(date, open, high, low, close, change, volume);
    }
  }
}


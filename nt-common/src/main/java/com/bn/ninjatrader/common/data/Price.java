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

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by Brad on 5/1/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Price implements DateObj<Price>, Serializable {
  public static final Builder builder() {
    return new Builder();
  }

  @JsonProperty("o")
  private double o; // open. Names are abbreviated to keep space usage low in datastore.

  @JsonProperty("h")
  private double h; // high

  @JsonProperty("l")
  private double l; // low

  @JsonProperty("c")
  private double c; // close

  @JsonProperty("v")
  private long v; // volume

  @JsonProperty("ch")
  private double ch; // change from previous price close

  @JsonProperty("d")
  @JsonSerialize(using = NtLocalDateSerializer.class)
  @JsonDeserialize(using = NtLocalDateDeserializer.class)
  @Translate(value = LocalDateTranslatorFactory.class)
  private LocalDate d; // date

  /**
   * Objectify requires private no-arg constructor.
   */
  private Price() {}

  public Price(final LocalDate date,
               final double open,
               final double high,
               final double low,
               final double close,
               final long volume) {
    this(date, open, high, low, close, 0d, volume);
  }

  public Price(@JsonSerialize(using = NtLocalDateSerializer.class)
               @JsonDeserialize(using = NtLocalDateDeserializer.class)
               @JsonProperty("d") final LocalDate date,
               @JsonProperty("o") final double open,
               @JsonProperty("h") final double high,
               @JsonProperty("l") final double low,
               @JsonProperty("c") final double close,
               @JsonProperty("ch") final double change,
               @JsonProperty("v") final long volume) {
    this.d = date;
    this.o = open;
    this.h = high;
    this.l = low;
    this.c = close;
    this.ch = change;
    this.v = volume;
  }

  @JsonIgnore
  public double getOpen() {
    return o;
  }

  @JsonIgnore
  public double getHigh() {
    return h;
  }

  @JsonIgnore
  public double getLow() {
    return l;
  }

  @JsonIgnore
  public double getClose() {
    return c;
  }

  @JsonIgnore
  public long getVolume() {
    return v;
  }

  public void setVolume(long volume) {
    this.v = volume;
  }

  @JsonIgnore
  public LocalDate getDate() {
    return d;
  }

  @JsonIgnore
  public double getChange() {
    return ch;
  }

  public void setChange(double change) {
    this.ch = change;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || obj.getClass() != getClass()) { return false; }
    if (obj == this) { return true; }
    final Price rhs = (Price) obj;
    return Objects.equal(d, rhs.d)
        && Objects.equal(o, rhs.o)
        && Objects.equal(h, rhs.h)
        && Objects.equal(l, rhs.l)
        && Objects.equal(c, rhs.c)
        && Objects.equal(ch, rhs.ch)
        && Objects.equal(v, rhs.v);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(d, o, h, l, c, ch, v);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("D", d).add("O", o).add("H", h).add("L", l).add("C", c).add("CH", ch).add("V", v)
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
      this.date = price.d;
      this.open = price.o;
      this.high = price.h;
      this.low = price.l;
      this.close = price.c;
      this.change = price.ch;
      this.volume = price.v;
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


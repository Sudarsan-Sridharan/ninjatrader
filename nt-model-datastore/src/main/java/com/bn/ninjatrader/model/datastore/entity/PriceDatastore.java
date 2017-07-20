package com.bn.ninjatrader.model.datastore.entity;

import com.bn.ninjatrader.common.util.NtLocalDateDeserializer;
import com.bn.ninjatrader.common.util.NtLocalDateSerializer;
import com.bn.ninjatrader.model.entity.Price;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.time.LocalDate;

/**
 * @author bradwee2000@gmail.com
 */
public class PriceDatastore extends Price {

  private double o; // open. Names are abbreviated to keep space usage low in datastore.

  private double h; // high

  private double l; // low

  private double c; // close

  private long v; // volume

  private double ch; // change from previous price close

//  @Translate(value = LocalDateTranslatorFactory.class)
//  private LocalDate d; // date

  private LocalDate d; // Appengine doesn't support java8 LocalDate. so use string for now.

  /**
   * Objectify requires private no-arg constructor.
   */
  private PriceDatastore() {
    this(null, 0, 0, 0, 0, 0, 0);
  }

  public PriceDatastore(@JsonSerialize(using = NtLocalDateSerializer.class)
                    @JsonDeserialize(using = NtLocalDateDeserializer.class)
                    @JsonProperty("d") final LocalDate date,
                    @JsonProperty("o") final double open,
                    @JsonProperty("h") final double high,
                    @JsonProperty("l") final double low,
                    @JsonProperty("c") final double close,
                    @JsonProperty("ch") final double change,
                    @JsonProperty("v") final long volume) {
    super(date, open, high, low, close, change, volume);
    this.d = date;
    this.o = open;
    this.h = high;
    this.l = low;
    this.c = close;
    this.ch = change;
    this.v = volume;
  }

  @Override
  public double getOpen() {
    return o;
  }

  @Override
  public double getHigh() {
    return h;
  }

  @Override
  public double getLow() {
    return l;
  }

  @Override
  public double getClose() {
    return c;
  }

  @Override
  public long getVolume() {
    return v;
  }

  @Override
  public LocalDate getDate() {
    return d;
  }

  @Override
  public double getChange() {
    return ch;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || obj.getClass() != getClass()) { return false; }
    if (obj == this) { return true; }
    final PriceDatastore rhs = (PriceDatastore) obj;
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

  @Override
  public int compareTo(final Price other) {
    return getDate().compareTo(other.getDate());
  }
}

package com.bn.ninjatrader.model.util;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.entity.PriceBuilder;
import com.bn.ninjatrader.model.entity.PriceBuilderFactory;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.time.LocalDate;

/**
 * @author bradwee2000@gmail.com
 */
public class DummyPriceBuilderFactory implements PriceBuilderFactory {
  @Override
  public PriceBuilder builder() {
    return new DummyPriceBuilder();
  }

  /**
   * Builder class
   */
  public static final class DummyPriceBuilder implements PriceBuilder {
    private LocalDate date;
    private double open;
    private double high;
    private double low;
    private double close;
    private long volume;
    private double change;

    public DummyPriceBuilder copyOf(final Price price) {
      this.date = price.getDate();
      this.open = price.getOpen();
      this.high = price.getHigh();
      this.low = price.getLow();
      this.close = price.getClose();
      this.change = price.getChange();
      this.volume = price.getVolume();
      return this;
    }
    public DummyPriceBuilder date(final LocalDate date) {
      this.date = date;
      return this;
    }
    public DummyPriceBuilder open(final double open) {
      this.open = open;
      return this;
    }
    public DummyPriceBuilder high(final double high) {
      this.high = high;
      return this;
    }
    public DummyPriceBuilder low(final double low) {
      this.low = low;
      return this;
    }
    public DummyPriceBuilder close(final double close) {
      this.close = close;
      return this;
    }
    public DummyPriceBuilder change(final double change) {
      this.change = change;
      return this;
    }
    public DummyPriceBuilder volume(final long volume) {
      this.volume = volume;
      return this;
    }
    public DummyPriceBuilder addVolume(final long volume) {
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
      return new DummyPrice(date, open, high, low, close, change, volume);
    }
  }

  /**
   * Dummy Price
   */
  public static final class DummyPrice implements Price {
    private final double o;
    private final double h;
    private final double l;
    private final double c;
    private final long v;
    private final double ch;
    private final LocalDate d;

    public DummyPrice(final LocalDate date,
                      final double open,
                      final double high,
                      final double low,
                      final double close,
                      final double change,
                      final long volume) {
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
      final DummyPrice rhs = (DummyPrice) obj;
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
  }
}

package com.bn.ninjatrader.common.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author bradwee2000@gmail.com
 */
public class Price implements DateObj<Price>, Serializable {

  public static final Builder builder() {
    return new Builder();
  }

  private double open;
  private double high;
  private double low;
  private double close;
  private long volume;
  private double change;
  private LocalDate date;

  private Price() {}

  public Price(final LocalDate date,
               final double open,
               final double high,
               final double low,
               final double close,
               final double change,
               final long volume) {
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

  public LocalDate getDate() {
    return date;
  }
  
  public double getChange() {
    return change;
  }
  
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || !(o instanceof Price)) return false;
    Price that = (Price) o;
    return Double.compare(that.getOpen(), open) == 0 &&
        Double.compare(that.getHigh(), high) == 0 &&
        Double.compare(that.getLow(), low) == 0 &&
        Double.compare(that.getClose(), close) == 0 &&
        volume == that.getVolume() &&
        Double.compare(that.getChange(), change) == 0 &&
        Objects.equal(date, that.getDate());
  }

  public int hashCode() {
    return Objects.hashCode(open, high, low, close, volume, change, date);
  }
  
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("D", date).add("O", open).add("H", high).add("L", low).add("C", close).add("CH", change).add("V", volume)
        .toString();
  }

  public int compareTo(final Price other) {
    return getDate().compareTo(other.getDate());
  }

  /**
   * Builder
   */
  public static class Builder {
    private LocalDate date;
    private double open;
    private double high;
    private double low;
    private double close;
    private long volume;
    private double change;

    public Builder copyOf(final Price price) {
      this.date = price.getDate();
      this.open = price.getOpen();
      this.high = price.getHigh();
      this.low = price.getLow();
      this.close = price.getClose();
      this.change = price.getChange();
      this.volume = price.getVolume();
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

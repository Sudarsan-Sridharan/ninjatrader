package com.bn.ninjatrader.common.model;

import com.bn.ninjatrader.common.util.NtLocalDateDeserializer;
import com.bn.ninjatrader.common.util.NtLocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by Brad on 4/27/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DailyQuote implements Serializable {
  private static final long serialVersionUID = 1L;

  private String symbol;

  @JsonSerialize(using = NtLocalDateSerializer.class)
  @JsonDeserialize(using = NtLocalDateDeserializer.class)
  private LocalDate date;
  private double open;
  private double high;
  private double low;
  private double close;
  private long volume;

  public DailyQuote() {}

  public DailyQuote(final String symbol,
                    @JsonDeserialize(using = NtLocalDateDeserializer.class)
                    LocalDate date,
                    double open,
                    double high,
                    double low,
                    double close,
                    long volume) {
    this.symbol = symbol;
    this.date = date;
    this.open = open;
    this.high = high;
    this.low = low;
    this.close = close;
    this.volume = volume;
  }

  public String getSymbol() {
    return symbol;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DailyQuote that = (DailyQuote) o;
    return Double.compare(that.open, open) == 0 &&
        Double.compare(that.high, high) == 0 &&
        Double.compare(that.low, low) == 0 &&
        Double.compare(that.close, close) == 0 &&
        volume == that.volume &&
        Objects.equal(symbol, that.symbol) &&
        Objects.equal(date, that.date);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(symbol, date, open, high, low, close, volume);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("symbol", symbol)
        .add("date", date)
        .add("open", open)
        .add("high", high)
        .add("low", low)
        .add("close", close)
        .add("volume", volume)
        .toString();
  }

  @JsonIgnore
  public Price getPrice() {
    return Price.builder()
        .date(date).open(open).high(high).low(low).close(close).volume(volume).build();
  }
}

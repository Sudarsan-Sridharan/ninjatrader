package com.bn.ninjatrader.model.mongo.entity;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.common.util.NtLocalDateDeserializer;
import com.bn.ninjatrader.common.util.NtLocalDateSerializer;
import com.bn.ninjatrader.model.mongo.builder.PriceBuilderMongo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.time.LocalDate;

/**
 * @author bradwee2000@gmail.com
 */
public class MongoPrice implements Price {

  public static final PriceBuilderMongo builder() {
    return new PriceBuilderMongo();
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

  public MongoPrice(@JsonSerialize(using = NtLocalDateSerializer.class)
                    @JsonDeserialize(using = NtLocalDateDeserializer.class)
                    @JsonProperty("d") final LocalDate date,
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

  @Override
  public double getOpen() {
    return open;
  }

  @Override
  public double getHigh() {
    return high;
  }

  @Override
  public double getLow() {
    return low;
  }

  @Override
  public double getClose() {
    return close;
  }

  @Override
  public long getVolume() {
    return volume;
  }

  @Override
  public LocalDate getDate() {
    return date;
  }

  @Override
  public double getChange() {
    return change;
  }

  @Override
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

  @Override
  public int hashCode() {
    return Objects.hashCode(open, high, low, close, volume, change, date);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("D", date).add("O", open).add("H", high).add("L", low).add("C", close).add("CH", change).add("V", volume)
        .toString();
  }

  @Override
  public int compareTo(final Price other) {
    return getDate().compareTo(other.getDate());
  }
}

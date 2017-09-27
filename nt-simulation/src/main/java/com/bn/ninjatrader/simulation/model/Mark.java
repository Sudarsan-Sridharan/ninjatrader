package com.bn.ninjatrader.simulation.model;

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
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Mark {
  private static final String DEFAULT_MARK_COLOR = "yellow";
  private static final Marker DEFAULT_MARKER = Marker.ARROW_TOP;

  public static final Mark onDate(final LocalDate date) {
    return new Mark(date, date, DEFAULT_MARK_COLOR, 0, DEFAULT_MARKER);
  }

  public static final Mark onDateRange(final LocalDate from, final LocalDate to) {
    return new Mark(from, to, DEFAULT_MARK_COLOR, 0, DEFAULT_MARKER);
  }

  @JsonProperty("date")
  @JsonSerialize(using = NtLocalDateSerializer.class)
  @JsonDeserialize(using = NtLocalDateDeserializer.class)
  private LocalDate date;

  @JsonProperty("date2")
  @JsonSerialize(using = NtLocalDateSerializer.class)
  @JsonDeserialize(using = NtLocalDateDeserializer.class)
  private LocalDate date2;

  @JsonProperty("color")
  private String color;

  @JsonProperty("price")
  private double price;

  @JsonProperty("marker")
  private Marker marker;

  public Mark(@JsonProperty("date")
              @JsonSerialize(using = NtLocalDateSerializer.class)
              @JsonDeserialize(using = NtLocalDateDeserializer.class)
              final LocalDate date,
              @JsonProperty("date2")
              @JsonSerialize(using = NtLocalDateSerializer.class)
              @JsonDeserialize(using = NtLocalDateDeserializer.class)
              final LocalDate date2,
              @JsonProperty("color") final String color,
              @JsonProperty("price") final double price,
              @JsonProperty("marker") final Marker marker) {
    this.date = date;
    this.date2 = date2;
    this.color = color;
    this.price = price;
    this.marker = marker;
  }

  public LocalDate getDate() {
    return date;
  }

  public String getColor() {
    return color;
  }

  public Mark withDate(final LocalDate date) {
    this.date = date;
    return this;
  }

  public Mark withDateRange(final LocalDate from, final LocalDate to) {
    this.date = from;
    this.date2 = to;
    return this;
  }

  public Mark withColor(final String color) {
    this.color = color;
    return this;
  }

  public Mark withPrice(final double price) {
    this.price = price;
    return this;
  }

  public Mark withMarker(final Marker marker) {
    this.marker = marker;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Mark mark = (Mark) o;
    return Double.compare(mark.price, price) == 0 &&
        Objects.equal(date, mark.date) &&
        Objects.equal(date2, mark.date2) &&
        Objects.equal(color, mark.color) &&
        marker == mark.marker;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(date, date2, color, price, marker);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("date", date)
        .add("date2", date2)
        .add("color", color)
        .add("price", price)
        .add("marker", marker)
        .toString();
  }
}

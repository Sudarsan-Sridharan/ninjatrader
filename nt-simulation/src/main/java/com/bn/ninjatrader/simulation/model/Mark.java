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
    return new Mark(date, DEFAULT_MARK_COLOR, DEFAULT_MARKER);
  }

  @JsonProperty("date")
  @JsonSerialize(using = NtLocalDateSerializer.class)
  @JsonDeserialize(using = NtLocalDateDeserializer.class)
  private final LocalDate date;

  @JsonProperty("color")
  private final String color;

  @JsonProperty("marker")
  private final Marker marker;

  public Mark(@JsonProperty("date")
              @JsonSerialize(using = NtLocalDateSerializer.class)
              @JsonDeserialize(using = NtLocalDateDeserializer.class)
              final LocalDate date,
              @JsonProperty("color") final String color,
              @JsonProperty("marker") final Marker marker) {
    this.date = date;
    this.color = color;
    this.marker = marker;
  }

  public LocalDate getDate() {
    return date;
  }

  public String getColor() {
    return color;
  }

  public Mark withDate(final LocalDate date) {
    return new Mark(date, this.color, this.marker);
  }

  public Mark withColor(final String color) {
    return new Mark(this.date, color, this.marker);
  }

  public Mark withMarker(final Marker marker) {
    return new Mark(this.date, this.color, marker);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof Mark)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final Mark rhs = (Mark) obj;
    return Objects.equal(date, rhs.date)
        && Objects.equal(color, rhs.color)
        && Objects.equal(marker, rhs.marker);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(date, color, marker);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("date", date).add("color", color).add("marker", marker).toString();
  }
}

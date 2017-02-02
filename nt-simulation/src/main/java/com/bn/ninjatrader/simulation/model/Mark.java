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

  public static final Mark onDate(final LocalDate date) {
    return new Mark(date, DEFAULT_MARK_COLOR);
  }

  @JsonProperty("date")
  @JsonSerialize(using = NtLocalDateSerializer.class)
  @JsonDeserialize(using = NtLocalDateDeserializer.class)
  private final LocalDate date;

  @JsonProperty("color")
  private final String color;

  public Mark(@JsonProperty("date")
              @JsonSerialize(using = NtLocalDateSerializer.class)
              @JsonDeserialize(using = NtLocalDateDeserializer.class)
              final LocalDate date,
              @JsonProperty("color") final String color) {
    this.date = date;
    this.color = color;
  }

  public LocalDate getDate() {
    return date;
  }

  public String getColor() {
    return color;
  }

  public Mark withDate(final LocalDate date) {
    return new Mark(date, this.color);
  }

  public Mark withColor(final String color) {
    return new Mark(this.date, color);
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
        && Objects.equal(color, rhs.color);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(date, color);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("date", date).add("color", color).toString();
  }
}

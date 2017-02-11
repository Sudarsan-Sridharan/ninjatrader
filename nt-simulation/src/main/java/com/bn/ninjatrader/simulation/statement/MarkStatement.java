package com.bn.ninjatrader.simulation.statement;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.History;
import com.bn.ninjatrader.simulation.model.Mark;
import com.bn.ninjatrader.simulation.model.Marker;
import com.bn.ninjatrader.simulation.model.World;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarkStatement implements Statement {
  private static final Logger LOG = LoggerFactory.getLogger(MarkStatement.class);
  private static final String DEFAULT_COLOR = "blue";
  private static final int DEFAULT_NUM_OF_BARS = 0;
  private static final Marker DEFAULT_MARKER = Marker.ARROW_TOP;

  public static final MarkStatement withMarker(final Marker marker) {
    return new MarkStatement(DEFAULT_COLOR, DEFAULT_NUM_OF_BARS, marker);
  }

  public static final MarkStatement withColor(final String color) {
    return new MarkStatement(color, DEFAULT_NUM_OF_BARS, DEFAULT_MARKER);
  }

  @JsonProperty("color")
  private final String color;

  @JsonProperty("numOfBarsAgo")
  private final int numOfBarsAgo;

  @JsonProperty("marker")
  private final Marker marker;

  public MarkStatement(@JsonProperty("color") final String color,
                       @JsonProperty("numOfBarsAgo") final int numOfBarsAgo,
                       @JsonProperty("marker") final Marker marker) {
    this.color = color;
    this.numOfBarsAgo = numOfBarsAgo;
    this.marker = marker;
  }

  @Override
  public void run(final BarData barData) {
    final World world = barData.getWorld();
    final History history = world.getHistory();
    final Optional<BarData> historyBarData = history.getNBarsAgo(numOfBarsAgo);
    if (historyBarData.isPresent()) {
      final Price price = historyBarData.get().getPrice();
      world.getChartMarks().add(Mark.onDate(price.getDate()).withColor(color).withMarker(marker));
    }
  }

  @JsonIgnore
  @Override
  public Set<Variable> getVariables() {
    return Collections.emptySet();
  }

  public MarkStatement color(final String color) {
    return new MarkStatement(color, numOfBarsAgo, marker);
  }

  public MarkStatement numOfBarsAgo(final int numOfBarsAgo) {
    return new MarkStatement(color, numOfBarsAgo, marker);
  }

  public MarkStatement marker(final Marker marker) {
    return new MarkStatement(color, numOfBarsAgo, marker);
  }

  public String getColor() {
    return color;
  }

  public int getNumOfBarsAgo() {
    return numOfBarsAgo;
  }

  public Marker getMarker() {
    return marker;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof MarkStatement)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final MarkStatement rhs = (MarkStatement) obj;
    return Objects.equal(color, rhs.color)
        && Objects.equal(numOfBarsAgo, rhs.numOfBarsAgo)
        && Objects.equal(marker, rhs.marker);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(color, numOfBarsAgo, marker);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("color", color).add("numOfBarsAgo", numOfBarsAgo).add("marker", marker).toString();
  }
}

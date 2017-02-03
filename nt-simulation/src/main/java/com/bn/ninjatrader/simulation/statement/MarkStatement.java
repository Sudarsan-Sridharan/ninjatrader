package com.bn.ninjatrader.simulation.statement;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.History;
import com.bn.ninjatrader.simulation.model.Mark;
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

  public static final Builder builder() {
    return new Builder();
  }

  @JsonProperty("color")
  private final String color;

  @JsonProperty("numOfBarsAgo")
  private final int numOfBarsAgo;

  public MarkStatement(@JsonProperty("color") final String color,
                       @JsonProperty("numOfBarsAgo") final int numOfBarsAgo) {
    this.color = color;
    this.numOfBarsAgo = numOfBarsAgo;
  }

  @Override
  public void run(final BarData barData) {
    final World world = barData.getWorld();
    final History history = world.getHistory();
    final Optional<BarData> historyBarData = history.getNBarsAgo(numOfBarsAgo);
    if (historyBarData.isPresent()) {
      final Price price = historyBarData.get().getPrice();
      world.getChartMarks().add(Mark.onDate(price.getDate()).withColor(color));
    }
  }

  @JsonIgnore
  @Override
  public Set<Variable> getVariables() {
    return Collections.emptySet();
  }

  public String getColor() {
    return color;
  }

  public int getNumOfBarsAgo() {
    return numOfBarsAgo;
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
        && Objects.equal(numOfBarsAgo, rhs.numOfBarsAgo);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(color, numOfBarsAgo);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("color", color).toString();
  }

  /**
   * Builder class
   */
  public static final class Builder {
    private String color = "blue";
    private int numOfBarsAgo;

    public Builder color(final String color) {
      this.color = color;
      return this;
    }

    public Builder numOfBarsAgo(final int numOfBarsAgo) {
      this.numOfBarsAgo = numOfBarsAgo;
      return this;
    }

    public MarkStatement build() {
      return new MarkStatement(color, numOfBarsAgo);
    }
  }
}
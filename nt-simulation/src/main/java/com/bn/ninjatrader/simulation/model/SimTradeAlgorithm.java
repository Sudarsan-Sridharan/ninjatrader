package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.logicexpression.statement.EmptyStatement;
import com.bn.ninjatrader.simulation.logicexpression.statement.Statement;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Set;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimTradeAlgorithm {
  public static final Builder builder() {
    return new Builder();
  }

  @JsonProperty("init")
  private final Statement init;

  @JsonProperty("play")
  private final Statement play;

  @JsonProperty("onBuyFulfilled")
  private final Statement onBuyFulfilled;

  @JsonProperty("onSellFulfilled")
  private final Statement onSellFulfilled;

  @JsonIgnore
  private final Set<Variable> variables;

  public SimTradeAlgorithm(@JsonProperty("init") final Statement init,
                           @JsonProperty("play") final Statement play,
                           @JsonProperty("onBuyFulfilled") final Statement onBuyFulfilled,
                           @JsonProperty("onSellFulfilled") final Statement onSellFulfilled) {
    this.init = init;
    this.play = play;
    this.onBuyFulfilled = onBuyFulfilled;
    this.onSellFulfilled = onSellFulfilled;

    // Collect variables
    final Set<Variable> vars = Sets.newHashSet();
    vars.addAll(init.getVariables());
    vars.addAll(play.getVariables());
    vars.addAll(onBuyFulfilled.getVariables());
    vars.addAll(onSellFulfilled.getVariables());
    variables = Collections.unmodifiableSet(vars);
  }

  public Statement getInit() {
    return init;
  }

  public Statement getPlay() {
    return play;
  }

  public Statement getOnBuyFulfilled() {
    return onBuyFulfilled;
  }

  public Statement getOnSellFulfilled() {
    return onSellFulfilled;
  }

  public Set<Variable> getVariables() {
    return variables;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SimTradeAlgorithm that = (SimTradeAlgorithm) o;
    return Objects.equal(init, that.init) &&
        Objects.equal(play, that.play) &&
        Objects.equal(onBuyFulfilled, that.onBuyFulfilled) &&
        Objects.equal(onSellFulfilled, that.onSellFulfilled);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(init, play, onBuyFulfilled, onSellFulfilled);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("init", init)
        .add("play", play)
        .add("onBuyFulfilled", onBuyFulfilled)
        .add("onSellFulfilled", onSellFulfilled)
        .toString();
  }

  /**
   * Builder
   */
  public static final class Builder {
    private Statement init = EmptyStatement.instance();
    private Statement play = EmptyStatement.instance();
    private Statement onBuyFulfilled = EmptyStatement.instance();
    private Statement onSellFulfilled = EmptyStatement.instance();

    public Builder init(final Statement init) {
      this.init = init;
      return this;
    }

    public Builder play(final Statement play) {
      this.play = play;
      return this;
    }

    public Builder onBuyFulfilled(final Statement onBuyFulfilled) {
      this.onBuyFulfilled = onBuyFulfilled;
      return this;
    }

    public Builder onSellFulfilled(final Statement onSellFulfilled) {
      this.onSellFulfilled = onSellFulfilled;
      return this;
    }

    public SimTradeAlgorithm build() {
      return new SimTradeAlgorithm(init, play, onBuyFulfilled, onSellFulfilled);
    }
  }
}

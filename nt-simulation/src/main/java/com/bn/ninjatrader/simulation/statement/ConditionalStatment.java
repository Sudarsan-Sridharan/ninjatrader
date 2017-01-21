package com.bn.ninjatrader.simulation.statement;

import com.bn.ninjatrader.simulation.condition.Condition;
import com.bn.ninjatrader.simulation.core.Simulation;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.operation.Variable;
import com.google.common.base.MoreObjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
public class ConditionalStatment implements Statement {
  private static final Logger LOG = LoggerFactory.getLogger(ConditionalStatment.class);

  public static final Builder builder() {
    return new Builder();
  }

  private final Condition condition;
  private final Statement thenStatement;
  private final Statement elseStatement;

  public ConditionalStatment(final Condition condition,
                             final Statement thenStatement,
                             final Statement elseStatement) {
    checkNotNull(condition, "condition must not be null.");
    this.condition = condition;
    this.thenStatement = thenStatement == null ? EmptyStatement.instance() : thenStatement;
    this.elseStatement = elseStatement == null? EmptyStatement.instance() : elseStatement;
  }

  @Override
  public void run(final Simulation simulation, final BarData barData) {
    if (condition.isMatch(barData)) {
      thenStatement.run(simulation, barData);
    } else {
      elseStatement.run(simulation, barData);
    }
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("condition", condition)
        .add("then", thenStatement)
        .add("else", elseStatement)
        .toString();
  }

  @Override
  public Set<Variable> getVariables() {
    return condition.getVariables();
  }

  /**
   * Builder class
   */
  public static final class Builder {
    private Condition condition;
    private Statement thenStatement;
    private Statement elseStatement;

    public Builder condition(final Condition condition) {
      this.condition = condition;
      return this;
    }
    public Builder then(final Statement thenStatement) {
      this.thenStatement = thenStatement;
      return this;
    }
    public Builder otherwise(final Statement elseStatement) {
      this.elseStatement = elseStatement;
      return this;
    }
    public ConditionalStatment build() {
      return new ConditionalStatment(condition, thenStatement, elseStatement);
    }
  }
}

package com.bn.ninjatrader.simulation.statement;

import com.bn.ninjatrader.logical.expression.condition.Condition;
import com.bn.ninjatrader.logical.expression.condition.FalseCondition;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.data.BarData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConditionalStatement implements Statement {
  private static final Logger LOG = LoggerFactory.getLogger(ConditionalStatement.class);

  public static final Builder builder() {
    return new Builder();
  }

  public static final ConditionalStatement withCondition(final Condition<BarData> condition) {
    return new ConditionalStatement(condition, EmptyStatement.instance(), EmptyStatement.instance(), "");
  }
  public static final ConditionalStatement withName(final String name) {
    return new ConditionalStatement(FalseCondition.instance(), EmptyStatement.instance(),
        EmptyStatement.instance(), name);
  }

  @JsonProperty("if")
  private final Condition condition;

  @JsonProperty("then")
  private final Statement thenStatement;

  @JsonProperty("else")
  private final Statement elseStatement;

  @JsonProperty("name")
  private final String name;

  public ConditionalStatement(@JsonProperty("if") final Condition<BarData> condition,
                              @JsonProperty("then") final Statement thenStatement,
                              @JsonProperty("else") final Statement elseStatement,
                              @JsonProperty("name") final String name) {
    checkNotNull(condition, "condition must not be null.");
    this.condition = condition;
    this.thenStatement = thenStatement == null ? EmptyStatement.instance() : thenStatement;
    this.elseStatement = elseStatement == null? EmptyStatement.instance() : elseStatement;
    this.name = name;
  }

  @Override
  public void run(final BarData barData) {
    if (condition.isMatch(barData)) {
      thenStatement.run(barData);
    } else {
      elseStatement.run(barData);
    }
  }

  public ConditionalStatement condition(final Condition condition) {
    return new ConditionalStatement(condition, thenStatement, elseStatement, name);
  }

  public ConditionalStatement name(final String name) {
    return new ConditionalStatement(condition, thenStatement, elseStatement, name);
  }

  public ConditionalStatement then(final Statement thenStatement) {
    return new ConditionalStatement(condition, thenStatement, elseStatement, name);
  }

  public ConditionalStatement otherwise(final Statement elseStatement) {
    return new ConditionalStatement(condition, thenStatement, elseStatement, name);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("name", name)
        .add("condition", condition)
        .add("then", thenStatement)
        .add("else", elseStatement)
        .toString();
  }

  @JsonIgnore
  @Override
  public Set<Variable> getVariables() {
    return condition.getVariables();
  }

  public String getName() {
    return name;
  }

  /**
   * Builder class
   */
  public static final class Builder {
    private Condition condition;
    private Statement thenStatement;
    private Statement elseStatement;
    private String name;

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

    public Builder name(final String name) {
      this.name = name;
      return this;
    }

    public ConditionalStatement build() {
      return new ConditionalStatement(condition, thenStatement, elseStatement, name);
    }
  }
}

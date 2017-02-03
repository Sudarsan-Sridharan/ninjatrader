package com.bn.ninjatrader.logical.expression.condition;

import com.bn.ninjatrader.logical.expression.model.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Brad on 8/5/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AndCondition<T extends Data> extends MultiCondition<AndCondition<T>, T> {
  private static final String AND = "AND";

  public AndCondition() {
    super();
  }

  public AndCondition(final Condition<T> condition1, final Condition<T> condition2) {
    super(condition1, condition2);
  }

  public AndCondition(final Condition<T> condition1, final Condition<T> condition2, final Condition<T> ... more) {
    super(condition1, condition2, more);
  }

  @Override
  public boolean isMatch(final T t) {
    if (getConditions().isEmpty()) {
      return true;
    }
    for (final Condition<T> condition : getConditions()) {
      if (!condition.isMatch(t)) {
        return false;
      }
    }
    return true;
  }

  @Override
  AndCondition<T> getThis() {
    return this;
  }

  @Override
  public String toString(final T t) {
    final StringBuilder sb = new StringBuilder("(");
    for (final Condition condition : getConditions()) {
      if (sb.length() != 1) {
        sb.append(" ").append(AND).append(" ");
      }
      sb.append(condition.toString(t));
    }
    sb.append(")");
    return sb.toString();
  }
}

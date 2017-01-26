package com.bn.ninjatrader.logical.expression.condition;

import com.bn.ninjatrader.logical.expression.model.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Brad on 8/5/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AndCondition<T extends Data> extends MultiCondition<AndCondition<T>, T> {

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
}

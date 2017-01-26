package com.bn.ninjatrader.logical.expression.condition;

import com.bn.ninjatrader.logical.expression.model.Data;
import com.bn.ninjatrader.logical.expression.operation.Variable;

import java.util.Collections;
import java.util.Set;

/**
 * Created by Brad on 8/5/16.
 */
public class FalseCondition<T extends Data> implements Condition<T> {
  private static FalseCondition INSTANCE;

  public static Condition instance() {
    if (INSTANCE == null) {
      INSTANCE = new FalseCondition();
    }
    return INSTANCE;
  }

  @Override
  public boolean isMatch(T t) {
    return false;
  }

  @Override
  public Set<Variable> getVariables() {
    return Collections.emptySet();
  }
}

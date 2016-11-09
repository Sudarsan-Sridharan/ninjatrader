package com.bn.ninjatrader.simulation.condition;

import com.bn.ninjatrader.simulation.operation.Variable;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Brad on 8/5/16.
 */
public abstract class MultiCondition<T> implements Condition {

  @JsonProperty("conditions")
  private List<Condition> conditions;

  public MultiCondition() {
    conditions = Lists.newArrayList();
  }

  public MultiCondition(Condition condition1, Condition condition2) {
    this();
    conditions.add(condition1);
    conditions.add(condition2);
  }

  public MultiCondition(Condition condition1, Condition condition2, Condition ... moreConditions) {
    this(condition1, condition2);
    conditions.addAll(Arrays.asList(moreConditions));
  }

  @Override
  public Set<Variable> getVariables() {
    Set<Variable> variables = Sets.newHashSet();
    for (Condition condition : conditions) {
      variables.addAll(condition.getVariables());
    }
    return variables;
  }

  public T add(Condition condition) {
    if (condition != null) {
      conditions.add(condition);
    }
    return getThis();
  }

  abstract T getThis();

  public List<Condition> getConditions() {
    return conditions;
  }

  public void setConditions(List<Condition> conditions) {
    this.conditions = conditions;
  }
}

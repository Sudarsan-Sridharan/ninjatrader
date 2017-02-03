package com.bn.ninjatrader.logical.expression.condition;

import com.bn.ninjatrader.logical.expression.model.Data;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Brad on 8/5/16.
 */
public abstract class MultiCondition<T, S extends Data> implements Condition<S> {

  @JsonProperty("conditions")
  private List<Condition<S>> conditions;

  public MultiCondition() {
    conditions = Lists.newArrayList();
  }

  public MultiCondition(final Condition<S> condition1, final Condition<S> condition2) {
    this();
    conditions.add(condition1);
    conditions.add(condition2);
  }

  public MultiCondition(final Condition<S> condition1, final Condition<S> condition2, final Condition<S> ... more) {
    this(condition1, condition2);
    conditions.addAll(Arrays.asList(more));
  }

  @Override
  public Set<Variable> getVariables() {
    final Set<Variable> variables = Sets.newHashSet();
    for (final Condition condition : conditions) {
      variables.addAll(condition.getVariables());
    }
    return variables;
  }

  public T add(final Condition condition) {
    if (condition != null) {
      conditions.add(condition);
    }
    return getThis();
  }

  abstract T getThis();

  public List<Condition<S>> getConditions() {
    return conditions;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof MultiCondition)) { return false; }
    if (obj == this) { return true; }
    final MultiCondition rhs = (MultiCondition) obj;
    return Objects.equal(conditions, rhs.conditions);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(conditions);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("conditions", conditions).toString();
  }
}

package com.bn.ninjatrader.testplay.condition;

import com.bn.ninjatrader.testplay.simulation.data.DataType;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Brad on 8/5/16.
 */
public abstract class MultiCondition<T> implements Condition {

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
  public Set<DataType> getDataTypes() {
    Set<DataType> dataTypes = Sets.newHashSet();
    for (Condition condition : conditions) {
      dataTypes.addAll(condition.getDataTypes());
    }
    return dataTypes;
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
}

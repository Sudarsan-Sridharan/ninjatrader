package com.bn.ninjatrader.logical.expression.condition;

import com.bn.ninjatrader.logical.expression.model.Data;
import com.bn.ninjatrader.logical.expression.operator.InequalityOperator;
import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 8/5/16.
 */
public class BasicCondition<T extends Data> implements Condition<T> {

  public static <T extends Data> BasicCondition<T> of (final Operation<T> lhs,
                                                       final InequalityOperator operator,
                                                       final Operation<T> rhs) {
    return new BasicCondition<T>(lhs, operator, rhs);
  }

  @JsonProperty("lhs")
  private final Operation lhs;

  @JsonProperty("rhs")
  private final Operation rhs;

  @JsonProperty("operator")
  private final InequalityOperator operator;

  public BasicCondition(@JsonProperty("lhs") Operation<T> lhs,
                        @JsonProperty("operator") InequalityOperator operator,
                        @JsonProperty("rhs") Operation<T> rhs) {
    checkNotNull(lhs, "LHS operation must not be null.");
    checkNotNull(operator, "Operator must not be null.");
    checkNotNull(rhs, "RHS operation must not be null.");

    this.lhs = lhs;
    this.rhs = rhs;
    this.operator = operator;
  }

  @Override
  public boolean isMatch(final T t) {
    double lhsValue = lhs.getValue(t);
    double rhsValue = rhs.getValue(t);
    return operator.isMatch(lhsValue, rhsValue);
  }

  @Override
  public Set<Variable> getVariables() {
    final Set<Variable> variables = Sets.newHashSet(lhs.getVariables());
    variables.addAll(rhs.getVariables());
    return variables;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("lhs", lhs).add("operator", operator).add("rhs", rhs)
        .toString();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof BasicCondition)) { return false; }
    if (obj == this) { return true; }
    final BasicCondition rhs = (BasicCondition) obj;
    return Objects.equal(lhs, rhs.lhs)
        && Objects.equal(this.rhs, rhs.rhs)
        && Objects.equal(operator, rhs.operator);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(lhs, rhs, operator);
  }
}

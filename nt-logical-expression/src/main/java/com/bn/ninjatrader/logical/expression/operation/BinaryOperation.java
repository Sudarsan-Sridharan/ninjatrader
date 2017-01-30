package com.bn.ninjatrader.logical.expression.operation;

import com.bn.ninjatrader.logical.expression.model.Data;
import com.bn.ninjatrader.logical.expression.operator.Operator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Created by Brad on 8/2/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BinaryOperation<T extends Data> implements Operation<T> {
  private static final Logger LOG = LoggerFactory.getLogger(BinaryOperation.class);

  public static final BinaryOperation of(final Operation lhs, final Operator operator, final Operation rhs) {
    return new BinaryOperation(lhs, operator, rhs);
  }
  public static final BinaryOperation of(final double lhs, final Operator operator, final Operation rhs) {
    return new BinaryOperation(Constant.of(lhs), operator, rhs);
  }
  public static final BinaryOperation of(final Operation lhs, final Operator operator, final double rhs) {
    return new BinaryOperation(lhs, operator, Constant.of(rhs));
  }
  public static final BinaryOperation of(final double lhs, final Operator operator, final double rhs) {
    return new BinaryOperation(Constant.of(lhs), operator, Constant.of(rhs));
  }

  @JsonProperty("lhs")
  private final Operation lhs; // Left-hand side

  @JsonProperty("rhs")
  private final Operation rhs; // Right-hand side

  @JsonProperty("operator")
  private final Operator operator;

  public BinaryOperation(@JsonProperty("lhs") final Operation lhs,
                         @JsonProperty("operator") final Operator operator,
                         @JsonProperty("rhs") final Operation rhs) {
    this.lhs = lhs;
    this.rhs = rhs;
    this.operator = operator;
  }

  @Override
  public double getValue(final T t) {
    return operator.exec(lhs, rhs, t);
  }

  @Override
  public Set<Variable> getVariables() {
    final Set<Variable> dataTypes = lhs.getVariables();
    dataTypes.addAll(rhs.getVariables());
    return dataTypes;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof BinaryOperation)) { return false; }
    if (obj == this) { return true; }
    final BinaryOperation rhsOp = (BinaryOperation) obj;
    return Objects.equal(lhs, rhsOp.lhs)
        && Objects.equal(rhs, rhsOp.rhs)
        && Objects.equal(operator, rhsOp.operator);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(lhs, rhs, operator);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("lhs", lhs).add("operator", operator).add("rhs", rhs).toString();
  }
}

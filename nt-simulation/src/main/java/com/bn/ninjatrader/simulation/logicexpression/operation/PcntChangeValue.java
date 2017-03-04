package com.bn.ninjatrader.simulation.logicexpression.operation;

import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.data.BarData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PcntChangeValue implements Operation<BarData> {
  private static final String TO_STRING = "pcntChange(%s)";
  private static final boolean DEFAULT_IS_RELATIVE_TO_LEFT = true;

  public static final PcntChangeValue of(final Operation<BarData> lhs, final Operation<BarData> rhs) {
    return new PcntChangeValue(lhs, rhs, DEFAULT_IS_RELATIVE_TO_LEFT);
  }

  @JsonProperty("lhs")
  private final Operation<BarData> lhs;

  @JsonProperty("rhs")
  private final Operation<BarData> rhs;

  @JsonProperty("isRelativeToLeft")
  private final boolean isRelativeToLeft;

  public PcntChangeValue(@JsonProperty("lhs") final Operation<BarData> lhs,
                         @JsonProperty("rhs") final Operation<BarData> rhs,
                         @JsonProperty("isRelativeToLeft") final boolean isRelativeToLeft) {
    this.lhs = lhs;
    this.rhs = rhs;
    this.isRelativeToLeft = isRelativeToLeft;
  }

  @Override
  public double getValue(final BarData barData) {
    final double lhsValue = lhs.getValue(barData);
    final double rhsValue = rhs.getValue(barData);
    final double diff = Math.abs(lhsValue - rhsValue);
    final double divisor = isRelativeToLeft ? lhsValue : rhsValue;
    return NumUtil.divide(diff, divisor);
  }

  @JsonIgnore
  @Override
  public Set<Variable> getVariables() {
    final Set<Variable> set = Sets.newHashSet();
    set.addAll(lhs.getVariables());
    set.addAll(rhs.getVariables());
    return set;
  }

  public PcntChangeValue relativeToRight() {
    return new PcntChangeValue(lhs, rhs, false);
  }

  public PcntChangeValue relativeToLeft() {
    return new PcntChangeValue(lhs, rhs, true);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof PcntChangeValue)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final PcntChangeValue other = (PcntChangeValue) obj;
    return Objects.equal(lhs, other.lhs)
        && Objects.equal(rhs, other.rhs)
        && Objects.equal(isRelativeToLeft, other.isRelativeToLeft);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(lhs, rhs, isRelativeToLeft);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("lhs", lhs).add("rhs", rhs).add("isRelativeToLeft", isRelativeToLeft).toString();
  }

  @Override
  public String toString(final BarData barData) {
    return String.format(TO_STRING, getValue(barData));
  }
}

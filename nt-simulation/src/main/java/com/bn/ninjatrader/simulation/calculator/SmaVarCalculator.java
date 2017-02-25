package com.bn.ninjatrader.simulation.calculator;

import com.bn.ninjatrader.calculator.util.SMACalculatingStack;
import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.data.DataMap;
import com.bn.ninjatrader.simulation.operation.Variables;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bradwee2000@gmail.com
 */
public class SmaVarCalculator implements VarCalculator {
  private static final Logger LOG = LoggerFactory.getLogger(SmaVarCalculator.class);

  public static final SmaVarCalculator forPeriod(final int period) {
    return new SmaVarCalculator(period);
  }

  private final int period;
  private final Variable variable;
  private final SMACalculatingStack stack;

  public SmaVarCalculator(final int period) {
    this.period = period;
    this.stack = SMACalculatingStack.withFixedSize(period);
    this.variable = Variables.EMA.withPeriod(period);
  }

  @Override
  public DataMap calc(final Price price) {
    stack.add(price);
    final double emaValue = stack.getValue();
    return DataMap.newInstance().addData(variable, emaValue);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SmaVarCalculator that = (SmaVarCalculator) o;
    return period == that.period &&
        Objects.equal(variable, that.variable);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(period, variable);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("period", period)
        .add("variable", variable)
        .toString();
  }
}

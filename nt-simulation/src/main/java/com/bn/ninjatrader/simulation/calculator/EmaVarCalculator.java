package com.bn.ninjatrader.simulation.calculator;

import com.bn.ninjatrader.calculator.util.EMACalculatingStack;
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
public class EmaVarCalculator implements VarCalculator {
  private static final Logger LOG = LoggerFactory.getLogger(EmaVarCalculator.class);

  public static final EmaVarCalculator forPeriod(final int period) {
    return new EmaVarCalculator(period);
  }

  private final int period;
  private final Variable variable;
  private final EMACalculatingStack stack;

  public EmaVarCalculator(final int period) {
    this.period = period;
    this.stack = EMACalculatingStack.withFixedSize(period);
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
    EmaVarCalculator that = (EmaVarCalculator) o;
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

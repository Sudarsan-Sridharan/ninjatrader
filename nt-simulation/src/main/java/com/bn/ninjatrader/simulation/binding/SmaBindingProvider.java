package com.bn.ninjatrader.simulation.binding;

import com.bn.ninjatrader.calculator.util.SMACalculatingStack;
import com.bn.ninjatrader.simulation.logic.Variable;
import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.simulation.data.DataMap;
import com.bn.ninjatrader.simulation.logic.Variables;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author bradwee2000@gmail.com
 */
public class SmaBindingProvider implements BindingProvider {
  private static final Logger LOG = LoggerFactory.getLogger(SmaBindingProvider.class);
  private static final String INVALID_PERIOD_MSG = "SMA period must be > 0.";

  public static final SmaBindingProvider forPeriod(final int period) {
    return new SmaBindingProvider(period);
  }

  private final int period;
  private final Variable variable;
  private final SMACalculatingStack stack;

  public SmaBindingProvider(final int period) {
    checkArgument(period > 0, INVALID_PERIOD_MSG);
    this.period = period;
    this.stack = SMACalculatingStack.withFixedSize(period);
    this.variable = Variables.SMA.withPeriod(period);
  }

  @Override
  public DataMap get(final Price price) {
    stack.add(price);
    final double emaValue = stack.getValue();
    return DataMap.newInstance().addData(variable, emaValue);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SmaBindingProvider that = (SmaBindingProvider) o;
    return period == that.period &&
        Objects.equal(variable, that.variable) &&
        Objects.equal(stack, that.stack);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(period, variable, stack);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("period", period)
        .add("variable", variable)
        .toString();
  }
}

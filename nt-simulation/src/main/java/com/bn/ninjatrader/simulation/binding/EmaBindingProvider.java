package com.bn.ninjatrader.simulation.binding;

import com.bn.ninjatrader.calculator.util.EMACalculatingStack;
import com.bn.ninjatrader.simulation.logic.Variable;
import com.bn.ninjatrader.common.model.Price;
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
public class EmaBindingProvider implements BindingProvider {
  private static final Logger LOG = LoggerFactory.getLogger(EmaBindingProvider.class);
  private static final String INVALID_PERIOD_MSG = "EMA period must be > 0.";

  public static final EmaBindingProvider forPeriod(final int period) {
    return new EmaBindingProvider(period);
  }

  private final int period;
  private final Variable variable;
  private final EMACalculatingStack stack;

  public EmaBindingProvider(final int period) {
    checkArgument(period > 0, INVALID_PERIOD_MSG);
    this.period = period;
    this.stack = EMACalculatingStack.withFixedSize(period);
    this.variable = Variables.EMA.withPeriod(period);
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
    EmaBindingProvider that = (EmaBindingProvider) o;
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

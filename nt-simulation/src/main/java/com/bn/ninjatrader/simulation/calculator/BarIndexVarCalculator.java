package com.bn.ninjatrader.simulation.calculator;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.simulation.data.DataMap;
import com.bn.ninjatrader.simulation.operation.Variables;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bradwee2000@gmail.com
 */
public class BarIndexVarCalculator implements VarCalculator {
  private static final Logger LOG = LoggerFactory.getLogger(BarIndexVarCalculator.class);
  private static final BarIndexVarCalculator INSTANCE = new BarIndexVarCalculator();

  public static final BarIndexVarCalculator instance() {
    return INSTANCE;
  }

  private long count = 0;

  public BarIndexVarCalculator() {}

  public BarIndexVarCalculator(final int period) {

  }

  @Override
  public DataMap calc(final Price price) {
    return DataMap.newInstance()
        .addData(Variables.BAR_INDEX, (double) count++);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(0);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("count", count)
        .toString();
  }
}

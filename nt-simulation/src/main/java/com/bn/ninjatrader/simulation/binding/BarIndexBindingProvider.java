package com.bn.ninjatrader.simulation.binding;

import com.bn.ninjatrader.common.model.Price;
import com.bn.ninjatrader.simulation.data.DataMap;
import com.bn.ninjatrader.simulation.logic.Variables;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bradwee2000@gmail.com
 */
public class BarIndexBindingProvider implements BindingProvider {
  private static final Logger LOG = LoggerFactory.getLogger(BarIndexBindingProvider.class);
  private static final BarIndexBindingProvider INSTANCE = new BarIndexBindingProvider();

  public static final BarIndexBindingProvider instance() {
    return INSTANCE;
  }

  private long count = 0;

  public BarIndexBindingProvider() {}

  public BarIndexBindingProvider(final int period) {

  }

  @Override
  public DataMap get(final Price price) {
    return DataMap.newInstance()
        .addData(Variables.BAR_INDEX, (double) count++);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BarIndexBindingProvider that = (BarIndexBindingProvider) o;
    return count == that.count;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(count);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("count", count)
        .toString();
  }
}

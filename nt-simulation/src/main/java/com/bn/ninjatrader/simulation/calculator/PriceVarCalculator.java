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
public class PriceVarCalculator implements VarCalculator {
  private static final Logger LOG = LoggerFactory.getLogger(PriceVarCalculator.class);
  private static final PriceVarCalculator INSTANCE = new PriceVarCalculator();

  public static final PriceVarCalculator instance() {
    return INSTANCE;
  }


  public PriceVarCalculator() {}

  public PriceVarCalculator(final int period) {
  }

  @Override
  public DataMap calc(final Price price) {
    return DataMap.newInstance()
        .addData(Variables.PRICE_OPEN, price.getOpen())
        .addData(Variables.PRICE_HIGH, price.getHigh())
        .addData(Variables.PRICE_LOW, price.getLow())
        .addData(Variables.PRICE_CLOSE, price.getClose())
        .addData(Variables.VOLUME, (double) price.getVolume());
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
        .toString();
  }
}

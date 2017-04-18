package com.bn.ninjatrader.simulation.binding;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.simulation.data.DataMap;
import com.bn.ninjatrader.simulation.logicexpression.Variables;
import com.google.common.base.MoreObjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bradwee2000@gmail.com
 */
public class PriceBindingProvider implements BindingProvider {
  private static final Logger LOG = LoggerFactory.getLogger(PriceBindingProvider.class);
  private static final PriceBindingProvider INSTANCE = new PriceBindingProvider();

  public static final PriceBindingProvider instance() {
    return INSTANCE;
  }

  public PriceBindingProvider() {}

  public PriceBindingProvider(final int period) {
  }

  @Override
  public DataMap get(final Price price) {
    return DataMap.newInstance()
        .addData(Variables.DATE, price.getDate())
        .addData(Variables.PRICE_OPEN, price.getOpen())
        .addData(Variables.PRICE_HIGH, price.getHigh())
        .addData(Variables.PRICE_LOW, price.getLow())
        .addData(Variables.PRICE_CLOSE, price.getClose())
        .addData(Variables.VOLUME, price.getVolume());
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .toString();
  }
}

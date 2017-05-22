package com.bn.ninjatrader.simulation.order.type;

import com.bn.ninjatrader.simulation.data.BarData;

/**
 * Created by Brad on 8/13/16.
 */
public interface OrderType {

  boolean isFulfillable(final BarData onSubmitBarData, final BarData currentBarData);

  double getFulfilledPrice(final BarData onSubmitBarData, final BarData currentBarData);

}

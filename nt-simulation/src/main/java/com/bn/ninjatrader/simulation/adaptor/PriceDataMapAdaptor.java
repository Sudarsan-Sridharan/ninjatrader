package com.bn.ninjatrader.simulation.adaptor;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.data.DataMap;
import com.google.common.base.Preconditions;
import com.google.inject.Singleton;

import static com.bn.ninjatrader.simulation.operation.Variables.*;

/**
 * Created by Brad on 8/17/16.
 */
@Singleton
public class PriceDataMapAdaptor implements DataMapAdaptor<Price> {

  @Override
  public DataMap toDataMap(Price price) {
    Preconditions.checkNotNull(price);

    DataMap dataMap = new DataMap();
    dataMap.put(PRICE_OPEN, price.getOpen());
    dataMap.put(PRICE_HIGH, price.getHigh());
    dataMap.put(PRICE_LOW, price.getLow());
    dataMap.put(PRICE_CLOSE, price.getClose());
    dataMap.put(VOLUME, price.getVolume());
    return dataMap;
  }
}

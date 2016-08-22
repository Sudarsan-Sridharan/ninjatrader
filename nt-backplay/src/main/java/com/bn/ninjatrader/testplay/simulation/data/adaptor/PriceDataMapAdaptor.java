package com.bn.ninjatrader.testplay.simulation.data.adaptor;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.testplay.simulation.data.DataMap;
import com.bn.ninjatrader.common.data.DataType;
import com.google.common.base.Preconditions;
import com.google.inject.Singleton;

/**
 * Created by Brad on 8/17/16.
 */
@Singleton
public class PriceDataMapAdaptor implements DataMapAdaptor<Price> {

  @Override
  public DataMap toDataMap(Price price) {
    Preconditions.checkNotNull(price);

    DataMap dataMap = new DataMap();
    dataMap.put(DataType.PRICE_OPEN, price.getOpen());
    dataMap.put(DataType.PRICE_HIGH, price.getHigh());
    dataMap.put(DataType.PRICE_LOW, price.getLow());
    dataMap.put(DataType.PRICE_CLOSE, price.getClose());
    dataMap.put(DataType.VOLUME, price.getVolume());
    return dataMap;
  }
}

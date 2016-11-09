package com.bn.ninjatrader.simulation.adaptor;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.data.DataType;
import com.bn.ninjatrader.simulation.operation.Variable;
import com.bn.ninjatrader.simulation.data.DataMap;
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
    dataMap.put(Variable.of(DataType.PRICE_OPEN), price.getOpen());
    dataMap.put(Variable.of(DataType.PRICE_HIGH), price.getHigh());
    dataMap.put(Variable.of(DataType.PRICE_LOW), price.getLow());
    dataMap.put(Variable.of(DataType.PRICE_CLOSE), price.getClose());
    dataMap.put(Variable.of(DataType.VOLUME), price.getVolume());
    return dataMap;
  }
}

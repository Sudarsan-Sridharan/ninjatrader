package com.bn.ninjatrader.testplay.simulation.data;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.testplay.simulation.data.adaptor.PriceDataMapAdaptor;
import com.bn.ninjatrader.common.data.DataType;

/**
 * Parameters for one bar (e.g. daily, weekly, monthly)
 *
 * Created by Brad on 8/2/16.
 */
public class BarData {

  private DataMap dataMap;
  private Price price;
  private PriceDataMapAdaptor priceDataMapAdaptor = new PriceDataMapAdaptor();

  public BarData() {
    dataMap = new DataMap();
  }

  public Double get(DataType dataType) {
    return dataMap.get(dataType);
  }

  public BarData put(Price price) {
    this.price = price;
    put(priceDataMapAdaptor.toDataMap(price));
    return this;
  }

  public BarData put(DataMap dataMap) {
    this.dataMap.put(dataMap.toMap());
    return this;
  }

  public Price getPrice() {
    return price;
  }

  public BarData clear() {
    dataMap.clear();
    price = null;
    return this;
  }
}

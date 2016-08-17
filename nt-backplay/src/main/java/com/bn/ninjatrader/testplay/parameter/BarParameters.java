package com.bn.ninjatrader.testplay.parameter;

import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.testplay.type.DataType;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Parameters for one bar (e.g. daily, weekly, monthly)
 *
 * Created by Brad on 8/2/16.
 */
public class BarParameters {

  private Map<DataType, Double> dataTypeToValueMap;
  private Price price;

  public BarParameters() {
    dataTypeToValueMap = Maps.newHashMap();
  }

  public double get(DataType dataType) {
    if (dataTypeToValueMap.containsKey(dataType)) {
      return dataTypeToValueMap.get(dataType);
    }
    throw new IllegalStateException("Value does not exist for the DataType: " + dataType);
  }

  public BarParameters put(Price price) {
    this.price = price;
    dataTypeToValueMap.put(DataType.PRICE_OPEN, price.getOpen());
    dataTypeToValueMap.put(DataType.PRICE_HIGH, price.getHigh());
    dataTypeToValueMap.put(DataType.PRICE_LOW, price.getLow());
    dataTypeToValueMap.put(DataType.PRICE_CLOSE, price.getClose());
    dataTypeToValueMap.put(DataType.VOLUME, (double) price.getVolume());
    return this;
  }

  public BarParameters put(Ichimoku ichimoku) {
    dataTypeToValueMap.put(DataType.CHIKOU, ichimoku.getChikou());
    dataTypeToValueMap.put(DataType.TENKAN, ichimoku.getTenkan());
    dataTypeToValueMap.put(DataType.KIJUN, ichimoku.getKijun());
    dataTypeToValueMap.put(DataType.SENKOU_A, ichimoku.getSenkouA());
    dataTypeToValueMap.put(DataType.SENKOU_B, ichimoku.getSenkouB());
    return this;
  }

  public Price getPrice() {
    return price;
  }

  public BarParameters clearValues() {
    dataTypeToValueMap.clear();
    price = null;
    return this;
  }
}

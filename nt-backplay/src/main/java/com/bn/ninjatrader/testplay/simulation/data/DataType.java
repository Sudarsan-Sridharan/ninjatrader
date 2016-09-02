package com.bn.ninjatrader.testplay.simulation.data;

import com.bn.ninjatrader.testplay.operation.Operation;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by Brad on 8/2/16.
 */
public enum DataType implements Operation{

  BAR_INDEX,
  BAR_LAST_BUY_INDEX,
  BAR_LAST_SELL_INDEX,

  CONSTANT,

  PRICE_OPEN,
  PRICE_HIGH,
  PRICE_LOW,
  PRICE_CLOSE,
  VOLUME,

  TENKAN,
  KIJUN,
  SENKOU_A,
  SENKOU_B,
  CHIKOU,

  SMA_10(10),
  SMA_15(15),
  SMA_20(20),
  SMA_21(21),
  SMA_50(50),
  SMA_100(100),
  SMA_200(200),

  RSI_10(10),
  RSI_14(14),
  RSI_20(20)

  ;

  private final int period;

  DataType() {
    this.period = 0;
  }

  DataType(int period) {
    this.period = period;
  }

  public int getPeriod() {
    return period;
  }

  @Override
  public double getValue(BarData barData) {
    return barData.get(this);
  }

  @Override
  public Set<DataType> getDataTypes() {
    return Sets.newHashSet(this);
  }
}

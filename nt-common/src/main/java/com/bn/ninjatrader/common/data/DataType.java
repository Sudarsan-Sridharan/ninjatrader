package com.bn.ninjatrader.common.data;

/**
 * Created by Brad on 8/2/16.
 */
public enum DataType {

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
  SMA_21(21);

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
}

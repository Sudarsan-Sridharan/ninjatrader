package com.bn.ninjatrader.calculator.parameter;

import com.bn.ninjatrader.common.data.Price;

import java.util.List;

/**
 * Created by Brad on 9/3/16.
 */
public class CalcParams {

  private List<Price> prices;
  private int[] periods;

  public static CalcParams withPrice(List<Price> prices) {
    return new CalcParams().price(prices);
  }

  public CalcParams price(List<Price> prices) {
    this.prices = prices;
    return this;
  }

  public CalcParams periods(int ... periods) {
    this.periods = periods;
    return this;
  }

  public int[] getPeriods() {
    return periods;
  }

  public List<Price> getPrices() {
    return prices;
  }
}

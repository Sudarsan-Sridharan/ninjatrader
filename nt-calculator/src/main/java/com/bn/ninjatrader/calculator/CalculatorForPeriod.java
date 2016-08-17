package com.bn.ninjatrader.calculator;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.data.Value;

import java.util.List;
import java.util.Map;

/**
 * Created by Brad on 7/28/16.
 */
public interface CalculatorForPeriod {

  public Map<Integer, List<Value>> calc(List<Price> priceList, int ... periods);
}

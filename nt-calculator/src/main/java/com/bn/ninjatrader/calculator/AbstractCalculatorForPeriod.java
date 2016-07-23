package com.bn.ninjatrader.calculator;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.data.Value;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by Brad on 5/28/16.
 */
public abstract class AbstractCalculatorForPeriod {

  private static final Logger log = LoggerFactory.getLogger(AbstractCalculatorForPeriod.class);

  public abstract List<Value> calc(List<Price> priceList, int period);

  public Map<Integer, List<Value>> calc(List<Price> priceList, int ... periods) {
    Map<Integer, List<Value>> periodToValuesMap = Maps.newHashMap();
    for (int period : periods) {
      periodToValuesMap.put(period, calc(priceList, period));
    }
    return periodToValuesMap;
  }
}

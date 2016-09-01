package com.bn.ninjatrader.calculator;

import com.bn.ninjatrader.calculator.util.CalculatingStack;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.data.Value;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by Brad on 5/28/16.
 */
public abstract class AbstractCalculatorForPeriod implements CalculatorForPeriod {

  private static final Logger log = LoggerFactory.getLogger(AbstractCalculatorForPeriod.class);

  public abstract List<Value> calc(List<Price> priceList, int period);

  @Override
  public Map<Integer, List<Value>> calc(List<Price> priceList, int ... periods) {
    Map<Integer, List<Value>> periodToValuesMap = Maps.newHashMap();
    for (int period : periods) {
      periodToValuesMap.put(period, calc(priceList, period));
    }
    return periodToValuesMap;
  }

  protected List<Value> calcWithStack(List<Price> priceList, CalculatingStack<Price> stack) {
    List<Value> resultList = Lists.newArrayList();
    for (Price price : priceList) {
      stack.add(price);

      double calcValue = stack.getValue();

      if (!Double.isNaN(calcValue)) {
        Value value = new Value(price.getDate(), calcValue);
        resultList.add(value);
      }
    }
    return resultList;
  }
}

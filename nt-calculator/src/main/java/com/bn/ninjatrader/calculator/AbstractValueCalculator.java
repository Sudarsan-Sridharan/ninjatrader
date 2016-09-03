package com.bn.ninjatrader.calculator;

import com.bn.ninjatrader.calculator.parameter.CalcParams;
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
public abstract class AbstractValueCalculator implements ValueCalculator {

  private static final Logger log = LoggerFactory.getLogger(AbstractValueCalculator.class);

  public abstract List<Value> calc(List<Price> priceList, int period);

  @Override
  public Map<Integer, List<Value>> calc(CalcParams params) {
    Map<Integer, List<Value>> periodToValuesMap = Maps.newHashMap();
    for (int period : params.getPeriods()) {
      periodToValuesMap.put(period, calc(params.getPrices(), period));
    }
    return periodToValuesMap;
  }

  public List<Value> calcWithStack(List<Price> priceList, CalculatingStack<Price> stack) {
    List<Value> resultList = Lists.newArrayList();
    for (Price price : priceList) {
      stack.add(price);

      double calcValue = stack.getValue();

      if (!Double.isNaN(calcValue)) {
        Value value = createValue(price, stack);
        resultList.add(value);
      }
    }
    return resultList;
  }

  public Value createValue(Price price, CalculatingStack<Price> stack) {
    return new Value(price.getDate(), stack.getValue());
  }
}

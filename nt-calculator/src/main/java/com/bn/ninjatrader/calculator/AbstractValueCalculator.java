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
public abstract class AbstractValueCalculator<T extends Value> implements ValueCalculator {
  private static final Logger LOG = LoggerFactory.getLogger(AbstractValueCalculator.class);

  public abstract List<Value> calcForPeriod(final CalcParams<T> params, final int period);

  @Override
  public Map<Integer, List<Value>> calc(final CalcParams params) {
    final Map<Integer, List<Value>> periodToValuesMap = Maps.newHashMap();
    final List<Integer> periods = params.getPeriods();

    for (final Integer period : periods) {
      periodToValuesMap.put(period, calcForPeriod(params, period));
    }
    return periodToValuesMap;
  }

  public List<Value> calcWithStack(final List<Price> priceList,
                                   final CalculatingStack<Price> stack) {
    final List<Value> resultList = Lists.newArrayList();
    for (final Price price : priceList) {
      stack.add(price);

      final double calcValue = stack.getValue();

      if (!Double.isNaN(calcValue)) {
        final Value value = createValue(price, stack);
        resultList.add(value);
      }
    }
    return resultList;
  }

  public Value createValue(final Price price,
                           final CalculatingStack<Price> stack) {
    return new Value(price.getDate(), stack.getValue());
  }
}

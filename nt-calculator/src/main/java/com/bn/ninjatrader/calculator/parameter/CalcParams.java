package com.bn.ninjatrader.calculator.parameter;

import com.bn.ninjatrader.common.model.Price;
import com.bn.ninjatrader.model.deprecated.Value;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Brad on 9/3/16.
 */
public class CalcParams<T extends Value> {

  public static <T extends Value> CalcParams withPrices(final Collection<Price> prices) {
    return new CalcParams<T>().price(prices);
  }

  public static <T extends Value> CalcParams withPrices(final Price price, final Price ... more) {
    return new CalcParams<T>().price(Lists.asList(price, more));
  }

  private final List<Price> prices = Lists.newArrayList();
  private final List<Integer> periods = Lists.newArrayList();
  private final Map<Integer, T> priorValues = Maps.newHashMap(); // Contains values to continue calculating from.

  public CalcParams price(final Collection<Price> prices) {
    if (prices != null) {
      this.prices.addAll(prices);
    }
    return this;
  }

  public CalcParams periods(final List<Integer> periods) {
    if (periods != null) {
      this.periods.addAll(periods);
    }
    return this;
  }

  public CalcParams periods(final Integer period, final Integer ... more) {
    this.periods.addAll(Lists.asList(period, more));
    return this;
  }

  public CalcParams addPriorValue(final int period, final T value) {
    priorValues.put(period, value);
    return this;
  }

  public CalcParams addAllPriorValues(final Map<Integer, T> values) {
    priorValues.putAll(values);
    return this;
  }

  public List<Integer> getPeriods() {
    return Lists.newArrayList(periods);
  }

  public List<Price> getPrices() {
    return Lists.newArrayList(prices);
  }

  public Optional<T> getPriorValueForPeriod(int period) {
    return Optional.ofNullable(priorValues.get(period));
  }
}

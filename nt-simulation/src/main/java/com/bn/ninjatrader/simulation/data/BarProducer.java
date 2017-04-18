package com.bn.ninjatrader.simulation.data;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.simulation.binding.BindingProvider;
import com.bn.ninjatrader.simulation.model.SimContext;

import java.util.Collection;
import java.util.List;

/**
 * Created by Brad on 8/24/16.
 */
public class BarProducer {

  private final Collection<BindingProvider> bindingProviders;

  private int index = 1;

  public BarProducer(final Collection<BindingProvider> bindingProviders) {
    this.bindingProviders = bindingProviders;
  }

  public BarData nextBar(final String symbol,
                         final Price price,
                         final SimContext simContext) {
    final BarData.Builder barDataBuilder = BarData.builder()
        .symbol(symbol)
        .index(index)
        .price(price)
        .world(simContext);

    bindingProviders.stream()
        .forEach(varCalculator -> barDataBuilder.addData(varCalculator.get(price)));

    index++;
    return barDataBuilder.build();
  }

  /**
   * Do some calculations prior to running the simulation.
   * Since some variables need past prices in order to calculate a value,
   * this ensures that variable values are ready.
   * @param preDatePrices
   */
  public void preCalc(final List<Price> preDatePrices) {
    for (final Price price : preDatePrices) {
      for (final BindingProvider varCalculator : bindingProviders) {
        varCalculator.get(price);
      }
    }
  }
}

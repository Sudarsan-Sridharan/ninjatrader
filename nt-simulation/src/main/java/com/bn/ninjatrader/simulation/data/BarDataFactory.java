package com.bn.ninjatrader.simulation.data;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.simulation.calculator.VarCalculator;
import com.bn.ninjatrader.simulation.model.World;
import com.google.inject.Singleton;

import java.util.List;

/**
 * Created by Brad on 8/24/16.
 */
@Singleton
public class BarDataFactory {

  public BarData create(final String symbol,
                        final Price price,
                        final int barIndex,
                        final World world,
                        final List<VarCalculator> varCalculators) {
    final BarData.Builder barDataBuilder = BarData.builder()
        .symbol(symbol)
        .index(barIndex)
        .price(price)
        .world(world);

    varCalculators.stream()
        .forEach(varCalculator -> barDataBuilder.addData(varCalculator.calc(price)));

    return barDataBuilder.build();
  }
}

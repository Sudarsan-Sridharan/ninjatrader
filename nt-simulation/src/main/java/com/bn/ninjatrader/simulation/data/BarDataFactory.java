package com.bn.ninjatrader.simulation.data;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.adaptor.PriceDataMapAdaptor;
import com.bn.ninjatrader.simulation.core.SimulationData;
import com.bn.ninjatrader.simulation.model.World;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.Collection;

import static com.bn.ninjatrader.simulation.operation.Variables.BAR_INDEX;

/**
 * Created by Brad on 8/24/16.
 */
@Singleton
public class BarDataFactory {
  private final PriceDataMapAdaptor priceDataMapAdaptor;

  @Inject
  public BarDataFactory(final PriceDataMapAdaptor priceDataMapAdaptor) {
    this.priceDataMapAdaptor = priceDataMapAdaptor;
  }

  public BarData create(final String symbol,
                        final Price price,
                        final int barIndex,
                        final Collection<SimulationData> dataCollection,
                        final World world) {
    final BarData.Builder barDataBuilder = BarData.builder()
        .symbol(symbol)
        .index(barIndex)
        .price(price)
        .world(world)
        .addData(priceDataMapAdaptor.toDataMap(price))
        .addData(BAR_INDEX, barIndex);

    for (final SimulationData data : dataCollection) {
      final DataMap dataMap = data.getDataAtIndex(barIndex);
      barDataBuilder.addData(dataMap);
    }
    return barDataBuilder.build();
  }
}

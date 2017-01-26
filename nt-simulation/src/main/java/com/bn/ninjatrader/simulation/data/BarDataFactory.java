package com.bn.ninjatrader.simulation.data;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.adaptor.PriceDataMapAdaptor;
import com.bn.ninjatrader.simulation.core.SimulationData;
import com.bn.ninjatrader.simulation.model.History;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.Collection;
import java.util.Collections;

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

  public BarData createWithPriceAtIndex(final Price price, final int barIndex) {
    return createWithPriceAtIndex(price, barIndex, Collections.emptyList(), null);
  }

  public BarData createWithPriceAtIndex(final Price price,
                                        final int barIndex,
                                        final Collection<SimulationData> dataCollection,
                                        final History history) {
    final BarData.Builder barDataBuilder = BarData.builder()
        .index(barIndex)
        .price(price)
        .history(history)
        .addData(priceDataMapAdaptor.toDataMap(price))
        .addData(BAR_INDEX, barIndex);

    for (final SimulationData data : dataCollection) {
      final DataMap dataMap = data.getDataAtIndex(barIndex);
      barDataBuilder.addData(dataMap);
    }
    return barDataBuilder.build();
  }
}

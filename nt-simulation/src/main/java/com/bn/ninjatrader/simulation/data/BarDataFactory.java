package com.bn.ninjatrader.simulation.data;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.adaptor.PriceDataMapAdaptor;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import java.util.Collection;
import java.util.List;

import static com.bn.ninjatrader.simulation.operation.Variables.BAR_INDEX;

/**
 * Created by Brad on 8/24/16.
 */
public class BarDataFactory {

  private final List<SimulationData> dataList = Lists.newArrayList();
  private final PriceDataMapAdaptor priceDataMapAdaptor;

  @Inject
  public BarDataFactory(final PriceDataMapAdaptor priceDataMapAdaptor) {
    this.priceDataMapAdaptor = priceDataMapAdaptor;
  }

  public BarData createWithPriceAtIndex(final Price price, final int barIndex) {
    final BarData.Builder barDataBuilder = BarData.builder()
        .index(barIndex)
        .price(price)
        .addData(priceDataMapAdaptor.toDataMap(price))
        .addData(BAR_INDEX, barIndex);

    for (final SimulationData data : dataList) {
      final DataMap dataMap = data.getDataAtIndex(barIndex);
      barDataBuilder.addData(dataMap);
    }
    return barDataBuilder.build();
  }

  public void addSimulationData(final Collection<SimulationData> dataList) {
    this.dataList.addAll(dataList);
  }

  public void addSimulationData(final SimulationData simulationData) {
    this.dataList.add(simulationData);
  }
}

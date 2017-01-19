package com.bn.ninjatrader.simulation.data;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.adaptor.PriceDataMapAdaptor;
import com.bn.ninjatrader.simulation.operation.Variable;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

/**
 * Created by Brad on 8/24/16.
 */
public class BarDataFactory {

  private final List<SimulationData> dataList = Lists.newArrayList();
  private final PriceDataMapAdaptor priceDataMapAdaptor = new PriceDataMapAdaptor();
  private final BarDataHistory barDataHistory = BarDataHistory.withMaxSize(52);
  private int barIndex = 0;

  public BarData create(final Price price) {
    final BarData barData = create(price, barIndex);
    barIndex++;
    return barData;
  }

  public BarData create(final Price price, int barIndex) {
    final BarData barData = BarData.forPrice(price);
    barData.setBarIndex(barIndex);
    barData.put(priceDataMapAdaptor.toDataMap(price));
    barData.put(Variable.of(DataType.BAR_INDEX), barIndex);

    for (final SimulationData data : dataList) {
      final DataMap dataMap = data.getDataMap(barIndex);
      barData.put(dataMap);
    }

    barDataHistory.add(barData);
    barData.setHistory(barDataHistory);

    barIndex++;
    return barData;
  }

  public void addSimulationData(final Collection<SimulationData> dataList) {
    this.dataList.addAll(dataList);
  }
}

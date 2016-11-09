package com.bn.ninjatrader.simulation.data;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.operation.Variable;
import com.bn.ninjatrader.simulation.adaptor.PriceDataMapAdaptor;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by Brad on 8/24/16.
 */
public class BarDataFactory {

  private List<SimulationData> dataList = Lists.newArrayList();
  private PriceDataMapAdaptor priceDataMapAdaptor = new PriceDataMapAdaptor();
  private BarDataHistory barDataHistory = BarDataHistory.withMaxSize(52);
  private int barIndex = 0;

  public BarData create(Price price) {
    BarData barData = create(price, barIndex);
    barIndex++;
    return barData;
  }

  public BarData create(Price price, int barIndex) {
    BarData barData = BarData.forPrice(price);
    barData.setBarIndex(barIndex);
    barData.put(priceDataMapAdaptor.toDataMap(price));
    barData.put(Variable.of(DataType.BAR_INDEX), barIndex);

    for (SimulationData data : dataList) {
      DataMap dataMap = data.getDataMap(barIndex);
      barData.put(dataMap);
    }

    barDataHistory.add(barData);
    barData.setHistory(barDataHistory);

    barIndex++;
    return barData;
  }

  public void addSimulationData(List<SimulationData> dataList) {
    this.dataList.addAll(dataList);
  }
}

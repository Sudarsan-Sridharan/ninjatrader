package com.bn.ninjatrader.testplay.simulation.data;

import com.bn.ninjatrader.common.data.DataType;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.testplay.simulation.broker.Broker;
import com.bn.ninjatrader.testplay.simulation.data.adaptor.PriceDataMapAdaptor;
import com.bn.ninjatrader.testplay.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.testplay.simulation.transaction.SellTransaction;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Contains data for one bar regardless if it's daily, weekly, or monthly time frame.
 *
 * Created by Brad on 8/2/16.
 */
public class BarData {

  private int barIndex;
  private DataMap dataMap;
  private Price price;
  private PriceDataMapAdaptor priceDataMapAdaptor = new PriceDataMapAdaptor();

  public BarData() {
    dataMap = new DataMap();
  }

  public Double get(DataType dataType) {
    return dataMap.get(dataType);
  }

  public BarData put(Price price) {
    this.price = price;
    put(priceDataMapAdaptor.toDataMap(price));
    return this;
  }

  public BarData put(DataMap dataMap) {
    this.dataMap.put(dataMap.toMap());
    return this;
  }

  public void put(Broker broker) {
    if (broker.getLastFulfilledBuy().isPresent()) {
      BuyTransaction lastBuy = broker.getLastFulfilledBuy().get();
      this.dataMap.put(DataType.BAR_LAST_BUY_INDEX, lastBuy.getBarIndex());
    }
    if (broker.getLastFulfilledSell().isPresent()) {
      SellTransaction lastSell = broker.getLastFulfilledSell().get();
      this.dataMap.put(DataType.BAR_LAST_SELL_INDEX, lastSell.getBarIndex());
    }
  }

  public BarData index(int barIndex) {
    this.barIndex = barIndex;
    this.dataMap.put(DataType.BAR_INDEX, barIndex);
    return this;
  }

  public int getBarIndex() {
    return barIndex;
  }

  public Price getPrice() {
    return price;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("dataMap", dataMap)
        .build();
  }
}

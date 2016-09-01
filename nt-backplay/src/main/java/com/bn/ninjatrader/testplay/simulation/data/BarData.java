package com.bn.ninjatrader.testplay.simulation.data;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.testplay.simulation.broker.Broker;
import com.bn.ninjatrader.testplay.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.testplay.simulation.transaction.SellTransaction;
import com.google.common.base.Optional;
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
  private BarDataHistory history;

  public static final BarData forPrice(Price price) {
    return new BarData(price);
  }

  public BarData() {
    dataMap = new DataMap();
  }

  private BarData(Price price) {
    this();
    this.price = price;
  }

  public Double get(DataType dataType) {
    return dataMap.get(dataType);
  }

  public BarData put(DataMap dataMap) {
    this.dataMap.put(dataMap.toMap());
    return this;
  }

  public BarData put(DataType dataType, double value) {
    this.dataMap.put(dataType, value);
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

  public int getBarIndex() {
    return barIndex;
  }

  public void setBarIndex(int barIndex) {
    this.barIndex = barIndex;
  }

  public Price getPrice() {
    return price;
  }

  public void setPrice(Price price) {
    this.price = price;
  }

  public void setHistory(BarDataHistory history) {
    this.history = history;
  }

  public Optional<BarData> getNBarsAgo(int numOfBarsAgo) {
    return history.getNBarsAgo(numOfBarsAgo);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("barIndex", barIndex)
        .append("price", price)
        .append("dataMap", dataMap)
        .build();
  }
}

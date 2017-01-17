package com.bn.ninjatrader.simulation.data;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.broker.Broker;
import com.bn.ninjatrader.simulation.operation.Variable;
import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.simulation.transaction.SellTransaction;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Optional;

/**
 * Contains data for one bar regardless if it's daily, weekly, or monthly time frame.
 * Data can include prices or any indicator values.
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

  public Double get(Variable variable) {
    return dataMap.get(variable);
  }

  public BarData put(DataMap dataMap) {
    this.dataMap.putAll(dataMap.toMap());
    return this;
  }

  public BarData put(Variable variable, double value) {
    this.dataMap.put(variable, value);
    return this;
  }

  public void put(Broker broker) {
    if (broker.getLastFulfilledBuy().isPresent()) {
      BuyTransaction lastBuy = broker.getLastFulfilledBuy().get();
      this.dataMap.put(Variable.of(DataType.BAR_LAST_BUY_INDEX), lastBuy.getBarIndex());
    }
    if (broker.getLastFulfilledSell().isPresent()) {
      SellTransaction lastSell = broker.getLastFulfilledSell().get();
      this.dataMap.put(Variable.of(DataType.BAR_LAST_SELL_INDEX), lastSell.getBarIndex());
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

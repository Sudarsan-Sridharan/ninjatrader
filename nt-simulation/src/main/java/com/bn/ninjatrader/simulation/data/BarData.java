package com.bn.ninjatrader.simulation.data;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.broker.Broker;
import com.bn.ninjatrader.simulation.operation.Variable;
import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.simulation.transaction.SellTransaction;
import com.google.common.base.MoreObjects;

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

  public static final BarData forPrice(final Price price) {
    return new BarData(price);
  }

  public BarData() {
    dataMap = new DataMap();
  }

  private BarData(final Price price) {
    this();
    this.price = price;
  }

  public Double get(final Variable variable) {
    return dataMap.get(variable);
  }

  public BarData put(final DataMap dataMap) {
    this.dataMap.putAll(dataMap.toMap());
    return this;
  }

  public BarData put(final Variable variable, final double value) {
    this.dataMap.put(variable, value);
    return this;
  }

  public void put(final Broker broker) {
    if (broker.getLastFulfilledBuy().isPresent()) {
      final BuyTransaction lastBuy = broker.getLastFulfilledBuy().get();
      this.dataMap.put(Variable.of(DataType.BAR_LAST_BUY_INDEX), lastBuy.getBarIndex());
    }
    if (broker.getLastFulfilledSell().isPresent()) {
      final SellTransaction lastSell = broker.getLastFulfilledSell().get();
      this.dataMap.put(Variable.of(DataType.BAR_LAST_SELL_INDEX), lastSell.getBarIndex());
    }
  }

  public int getBarIndex() {
    return barIndex;
  }

  public void setBarIndex(final int barIndex) {
    this.barIndex = barIndex;
  }

  public Price getPrice() {
    return price;
  }

  public void setPrice(final Price price) {
    this.price = price;
  }

  public void setHistory(final BarDataHistory history) {
    this.history = history;
  }

  public Optional<BarData> getNBarsAgo(final int numOfBarsAgo) {
    return history.getNBarsAgo(numOfBarsAgo);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("barIndex", barIndex)
        .add("price", price)
        .add("dataMap", dataMap)
        .toString();
  }
}

package com.bn.ninjatrader.simulation.data;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.logical.expression.model.Data;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.model.History;
import com.google.common.base.MoreObjects;

/**
 * Contains data for one bar regardless if it's daily, weekly, or monthly time frame.
 * Data can include prices or any indicator values.
 *
 * Created by Brad on 8/2/16.
 */
public class BarData implements Data {
  public static final Builder builder() {
    return new Builder();
  }

  private final int index;
  private final DataMap dataMap;
  private final Price price;
  private final History history;

  private BarData(final int index, final Price price, final DataMap dataMap, final History history) {
    this.index = index;
    this.price = price;
    this.dataMap = dataMap;
    this.history = history;
  }

  public Double get(final Variable variable) {
    return dataMap.get(variable);
  }

  public int getIndex() {
    return index;
  }

  public Price getPrice() {
    return price;
  }

  public History getHistory() {
    return history;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("index", index).add("price", price).add("dataMap", dataMap)
        .toString();
  }

  /**
   * Builder class
   */
  public static final class Builder {
    private int index;
    private DataMap dataMap = new DataMap();
    private Price price;
    private History history;

    public Builder index(final int index) {
      this.index = index;
      return this;
    }
    public Builder price(final Price price) {
      this.price = price;
      return this;
    }
    public Builder addData(final DataMap dataMap) {
      this.dataMap.putAll(dataMap.toMap());
      return this;
    }
    public Builder addData(final Variable variable, final double value) {
      this.dataMap.put(variable, value);
      return this;
    }
    public Builder history(final History history) {
      this.history = history;
      return this;
    }
    public BarData build() {
      return new BarData(index, price, dataMap, history);
    }
  }
}

package com.bn.ninjatrader.simulation.data;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.operation.Variable;
import com.google.common.base.MoreObjects;

/**
 * Contains data for one bar regardless if it's daily, weekly, or monthly time frame.
 * Data can include prices or any indicator values.
 *
 * Created by Brad on 8/2/16.
 */
public class BarData {
  public static final Builder builder() {
    return new Builder();
  }

  private int index;
  private final DataMap dataMap = new DataMap();
  private Price price;

  private BarData(final int index, final Price price, final DataMap dataMap) {
    this.index = index;
    this.price = price;
    this.dataMap.putAll(dataMap);
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

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("index", index)
        .add("price", price)
        .add("dataMap", dataMap)
        .toString();
  }

  /**
   * Builder class
   */
  public static final class Builder {
    private int index;
    private DataMap dataMap = new DataMap();
    private Price price;

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
    public BarData build() {
      return new BarData(index, price, dataMap);
    }
  }
}

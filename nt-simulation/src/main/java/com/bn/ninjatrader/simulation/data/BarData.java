package com.bn.ninjatrader.simulation.data;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.logical.expression.model.Data;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.model.World;
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

  private final String symbol;
  private final int index;
  private final DataMap dataMap;
  private final Price price;
  private final World world;

  private BarData(final String symbol,
                  final int index,
                  final Price price,
                  final DataMap dataMap,
                  final World world) {
    this.symbol = symbol;
    this.index = index;
    this.price = price;
    this.dataMap = dataMap;
    this.world = world;
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

  public World getWorld() {
    return world;
  }

  public String getSymbol() {
    return symbol;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("symbol", symbol).add("index", index).add("price", price).add("dataMap", dataMap).add("world", world)
        .toString();
  }

  /**
   * Builder class
   */
  public static final class Builder {
    private int index;
    private DataMap dataMap = new DataMap();
    private Price price;
    private World world;
    private String symbol;

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
    public Builder world(final World world) {
      this.world = world;
      return this;
    }
    public Builder symbol(final String symbol) {
      this.symbol = symbol;
      return this;
    }

    public BarData build() {
      return new BarData(symbol, index, price, dataMap, world);
    }
  }
}

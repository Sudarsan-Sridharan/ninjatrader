package com.bn.ninjatrader.simulation.data;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.logical.expression.model.Data;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.model.SimContext;
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
  private final SimContext simContext;

  private BarData(final String symbol,
                  final int index,
                  final Price price,
                  final DataMap dataMap,
                  final SimContext simContext) {
    this.symbol = symbol;
    this.index = index;
    this.price = price;
    this.dataMap = dataMap;
    this.simContext = simContext;
  }

  public Object get(final Variable variable) {
    return dataMap.get(variable);
  }

  public Object get2(final String variable) { return dataMap.get2(variable); }

  public int getIndex() {
    return index;
  }

  public Price getPrice() {
    return price;
  }

  public SimContext getSimContext() {
    return simContext;
  }

  public String getSymbol() {
    return symbol;
  }

  public DataMap getDataMap() {
    return dataMap;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("symbol", symbol).add("index", index).add("price", price).add("dataMap", dataMap).add("world", simContext)
        .toString();
  }

  /**
   * Builder class
   */
  public static final class Builder {
    private int index;
    private DataMap dataMap = new DataMap();
    private Price price;
    private SimContext simContext;
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
    public Builder world(final SimContext simContext) {
      this.simContext = simContext;
      return this;
    }
    public Builder symbol(final String symbol) {
      this.symbol = symbol;
      return this;
    }

    public BarData build() {
      return new BarData(symbol, index, price, dataMap, simContext);
    }
  }
}

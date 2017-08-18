package com.bn.ninjatrader.simulation.data;

import com.bn.ninjatrader.simulation.logic.Variable;
import com.bn.ninjatrader.common.model.Price;
import com.bn.ninjatrader.simulation.model.SimulationContext;
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

  private final String symbol;
  private final int index;
  private final DataMap dataMap;
  private final Price price;
  private final SimulationContext simulationContext;

  private BarData(final String symbol,
                  final int index,
                  final Price price,
                  final DataMap dataMap,
                  final SimulationContext simulationContext) {
    this.symbol = symbol;
    this.index = index;
    this.price = price;
    this.dataMap = dataMap;
    this.simulationContext = simulationContext;
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

  public SimulationContext getSimulationContext() {
    return simulationContext;
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
        .add("symbol", symbol).add("index", index).add("price", price).add("dataMap", dataMap).add("world", simulationContext)
        .toString();
  }

  /**
   * Builder class
   */
  public static final class Builder {
    private int index;
    private DataMap dataMap = new DataMap();
    private Price price;
    private SimulationContext simulationContext;
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
    public Builder world(final SimulationContext simulationContext) {
      this.simulationContext = simulationContext;
      return this;
    }
    public Builder symbol(final String symbol) {
      this.symbol = symbol;
      return this;
    }

    public BarData build() {
      return new BarData(symbol, index, price, dataMap, simulationContext);
    }
  }
}

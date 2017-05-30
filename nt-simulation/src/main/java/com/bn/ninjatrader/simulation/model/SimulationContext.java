package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.common.boardlot.BoardLotTable;
import com.bn.ninjatrader.model.entity.Price;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * @author bradwee2000@gmail.com
 */
public class SimulationContext {
  public static final Builder builder() {
    return new Builder();
  }

  private final Account account;
  private final Broker broker;
  private final BoardLotTable boardLotTable;
  private final History history;
  private final Map<String, List<Price>> prices;
  private final List<Mark> chartMarks = Lists.newArrayList();

  private SimulationContext(final Account account,
                            final Broker broker,
                            final BoardLotTable boardLotTable,
                            final History history,
                            final Map<String, List<Price>> prices) {
    this.account = account;
    this.broker = broker;
    this.boardLotTable = boardLotTable;
    this.history = history;
    this.prices = prices;
  }

  public Account getAccount() {
    return account;
  }

  public Broker getBroker() {
    return broker;
  }

  public BoardLotTable getBoardLotTable() {
    return boardLotTable;
  }

  public History getHistory() {
    return history;
  }

  public Map<String, List<Price>> getPrices() {
    return prices;
  }

  public List<Mark> getChartMarks() {
    return chartMarks;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("account", account)
        .add("broker", broker)
        .add("history", history)
        .add("priceSize", prices.size())
        .add("chartMarks", chartMarks)
        .toString();
  }

  /**
   * Builder class
   */
  public static final class Builder {
    private Account account;
    private Broker broker;
    private BoardLotTable boardLotTable;
    private History history;
    private Map<String, List<Price>> prices = Maps.newHashMap();

    public Builder account(final Account account) {
      this.account = account;
      return this;
    }
    public Builder broker(final Broker broker) {
      this.broker = broker;
      return this;
    }

    public Builder boardLotTable(final BoardLotTable boardLotTable) {
      this.boardLotTable = boardLotTable;
      return this;
    }

    public Builder history(final History history) {
      this.history = history;
      return this;
    }

    public Builder pricesForSymbol(final String symbol, final List<Price> prices) {
      this.prices.put(symbol, prices);
      return this;
    }

    public SimulationContext build() {
      return new SimulationContext(account, broker, boardLotTable, history, prices);
    }
  }
}

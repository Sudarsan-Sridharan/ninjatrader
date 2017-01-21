package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.account.Account;
import com.bn.ninjatrader.simulation.broker.Broker;
import com.bn.ninjatrader.simulation.data.History;

import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
public class World {
  public static final Builder builder() {
    return new Builder();
  }

  private final Account account;
  private final Broker broker;
  private final History history;
  private final List<Price> prices;

  private World(final Account account,
                final Broker broker,
                final History history,
                final List<Price> prices) {
    this.account = account;
    this.broker = broker;
    this.history = history;
    this.prices = prices;
  }

  public Account getAccount() {
    return account;
  }

  public Broker getBroker() {
    return broker;
  }

  public History getHistory() {
    return history;
  }

  public List<Price> getPrices() {
    return prices;
  }

  /**
   * Builder class
   */
  public static final class Builder {
    private Account account;
    private Broker broker;
    private History history;
    private List<Price> prices;

    public Builder account(final Account account) {
      this.account = account;
      return this;
    }
    public Builder broker(final Broker broker) {
      this.broker = broker;
      return this;
    }
    public Builder history(final History history) {
      this.history = history;
      return this;
    }
    public Builder prices(final List<Price> prices) {
      this.prices = prices;
      return this;
    }
    public World build() {
      return new World(account, broker, history, prices);
    }
  }
}

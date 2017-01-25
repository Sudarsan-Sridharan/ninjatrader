package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.simulation.account.Account;
import com.bn.ninjatrader.simulation.broker.Broker;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.data.History;

/**
 * @author bradwee2000@gmail.com
 */
public class Data {

  public static final Data of(final Account account, final Broker broker, final History history) {
    return new Data(account, broker, history);
  }

  private final Account account;
  private final Broker broker;
  private final History history;

  private BarData barData;

  private Data(final Account account, final Broker broker, final History history) {
    this.account = account;
    this.broker = broker;
    this.history = history;
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

  public BarData getBarData() {
    return barData;
  }

  public void setBarData(final BarData barData) {
    this.barData = barData;
  }
}

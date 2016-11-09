package com.bn.ninjatrader.simulation.broker;

import com.bn.ninjatrader.simulation.account.Account;

/**
 * Created by Brad on 8/22/16.
 */
public interface BrokerFactory {

  Broker createBroker(Account account);
}

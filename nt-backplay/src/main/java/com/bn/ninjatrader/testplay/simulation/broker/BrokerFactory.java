package com.bn.ninjatrader.testplay.simulation.broker;

import com.bn.ninjatrader.testplay.simulation.account.Account;

/**
 * Created by Brad on 8/22/16.
 */
public interface BrokerFactory {

  Broker createBroker(Account account);
}

package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.simulation.core.SimulationRequest;

/**
 * Created by Brad on 8/22/16.
 */
public interface BrokerFactory {

  Broker createBroker(final SimulationRequest isDebug);
}

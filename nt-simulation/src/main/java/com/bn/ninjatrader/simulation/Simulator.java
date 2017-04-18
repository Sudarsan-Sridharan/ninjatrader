package com.bn.ninjatrader.simulation;

import com.bn.ninjatrader.simulation.core.Simulation;
import com.bn.ninjatrader.simulation.core.SimulationFactory;
import com.bn.ninjatrader.simulation.core.SimulationRequest;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Brad on 8/3/16.
 */
@Singleton
public class Simulator {
  private static final Logger LOG = LoggerFactory.getLogger(Simulator.class);

  private final SimulationFactory simulationFactory;

  @Inject
  public Simulator(final SimulationFactory simulationFactory) {
    this.simulationFactory = simulationFactory;
  }

  public SimulationReport play(final SimulationRequest req) {
    final Simulation simulation = simulationFactory.create(req);
    final SimulationReport simReport = simulation.play();
    return simReport;
  }
}

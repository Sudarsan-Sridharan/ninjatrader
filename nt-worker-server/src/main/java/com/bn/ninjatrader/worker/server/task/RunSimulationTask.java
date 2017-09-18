package com.bn.ninjatrader.worker.server.task;

import com.bn.ninjatrader.simulation.core.Simulation;
import com.bn.ninjatrader.simulation.core.SimulationFactory;
import com.bn.ninjatrader.simulation.core.SimulationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
@Path("/task/run-simulation")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RunSimulationTask {
  private static final Logger LOG = LoggerFactory.getLogger(RunSimulationTask.class);

  private SimulationFactory simulationFactory;

  @Inject
  public RunSimulationTask() {

  }

  @POST
  public void run(final SimulationRequest simulationRequest) {
    final Simulation simulation = simulationFactory.create(simulationRequest);



  }
}

package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.simulation.core.Simulation;
import com.bn.ninjatrader.simulation.core.SimulationFactory;
import com.bn.ninjatrader.simulation.core.SimulationRequest;
import com.bn.ninjatrader.simulation.exception.AlgorithmIdNotFoundException;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.bn.ninjatrader.simulation.script.AlgorithmScript;
import com.bn.ninjatrader.simulation.service.AlgorithmService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
@Path("/task/simulation")
public class RunSimulationTask {
  private static final Logger LOG = LoggerFactory.getLogger(RunSimulationTask.class);
  private static final String ERROR_SYM_PARAM_REQUIRED = "symbol parameter is required.";
  private static final String ERROR_ALGO_ID_PARAM_REQUIRED = "algoId parameter is required.";
  private static final String ERROR_ALGO_ID_NOT_FOUND = "algoId is not found.";

  private final SimulationFactory simulationFactory;
  private final AlgorithmService algorithmService;

  @Inject
  public RunSimulationTask(final SimulationFactory simulationFactory,
                           final AlgorithmService algorithmService) {
    this.simulationFactory = simulationFactory;
    this.algorithmService = algorithmService;
  }

  @GET
  @Path("/run")
  @Produces(MediaType.APPLICATION_JSON)
  public Response runSimulation(@QueryParam("from") final String basicIsoFromDate,
                                @QueryParam("to") final String basicIsoToDate,
                                @QueryParam("symbol") final String symbol,
                                @QueryParam("algoId") final String algoId) {
    // Preconditions
    if (StringUtils.isEmpty(symbol)) {
      throw new BadRequestException(ERROR_SYM_PARAM_REQUIRED);
    }
    if (StringUtils.isEmpty(algoId)) {
      throw new BadRequestException(ERROR_ALGO_ID_PARAM_REQUIRED);
    }

    // Get from and to dates
    final LocalDate from = StringUtils.isEmpty(basicIsoFromDate) ? null :
        LocalDate.parse(basicIsoFromDate, DateTimeFormatter.BASIC_ISO_DATE);

    final LocalDate to = StringUtils.isEmpty(basicIsoToDate) ? null :
        LocalDate.parse(basicIsoToDate, DateTimeFormatter.BASIC_ISO_DATE);

    // Get Algorithm
    final AlgorithmScript script = algorithmService.findById(algoId);

    // Play simulation
    try {
      final Simulation simulation = simulationFactory.create(SimulationRequest.withSymbol(symbol)
          .from(from).to(to).algorithmScript(script));
      final SimulationReport report = simulation.play();
      return Response.ok(report).build();
    } catch (final AlgorithmIdNotFoundException e) {
      throw new BadRequestException(ERROR_ALGO_ID_NOT_FOUND);
    }
  }
}

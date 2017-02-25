package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.simulation.Simulator;
import com.bn.ninjatrader.simulation.TradeAlgorithm;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
@Path("/task/simulation")
public class RunSimulationTask {
  private static final Logger LOG = LoggerFactory.getLogger(RunSimulationTask.class);

  private final Simulator simulator;
  private final Clock clock;

  @Inject
  public RunSimulationTask(final Simulator simulator,
                           final Clock clock) {
    this.simulator = simulator;
    this.clock = clock;
  }

  @GET
  @Path("/run")
  @Produces(MediaType.APPLICATION_JSON)
  public Response runSimulation(@QueryParam("from") final String basicIsoFromDate,
                                @QueryParam("to") final String basicIsoToDate,
                                @QueryParam("symbol") final String symbol) {
    if (StringUtils.isEmpty(symbol)) {
      throw new BadRequestException("symbol parameter is required.");
    }

    final LocalDate from = StringUtils.isEmpty(basicIsoFromDate) ?
        LocalDate.now(clock).minusYears(2) :
        LocalDate.parse(basicIsoFromDate, DateTimeFormatter.BASIC_ISO_DATE);

    final LocalDate to = StringUtils.isEmpty(basicIsoToDate) ?
        LocalDate.now(clock) :
        LocalDate.parse(basicIsoToDate, DateTimeFormatter.BASIC_ISO_DATE);

    final SimulationReport report = simulator.play(TradeAlgorithm.newInstance()
        .from(from).to(to).forSymbol(symbol).build());

    return Response.ok(report).build();
  }
}

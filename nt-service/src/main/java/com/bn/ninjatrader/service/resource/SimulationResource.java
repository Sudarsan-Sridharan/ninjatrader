package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.common.data.Report;
import com.bn.ninjatrader.model.dao.ReportDao;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Clock;
import java.util.Optional;

import static com.bn.ninjatrader.model.request.FindReportRequest.withReportId;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
@Path("/simulation")
public class SimulationResource extends AbstractDataResource {

  private final ObjectMapper om = new ObjectMapper();

  private final ReportDao reportDao;

  @Inject
  public SimulationResource(final ReportDao reportDao,
                            final Clock clock) {
    super(clock);
    this.reportDao = reportDao;
  }

  @GET
  @Path("/report/{reportId}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getReport(@PathParam("reportId") final String reportId) {
    final Optional<Report> foundReport = reportDao.findOne(withReportId(reportId));

    if (foundReport.isPresent()) {
      return Response.ok(om.convertValue(foundReport.get().getData(), SimulationReport.class))
          .header("Access-Control-Allow-Origin", "*")
          .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
          .header("Access-Control-Allow-Methods", "GET")
          .build();
    }
    throw new NotFoundException(String.format("Report with ID [%s] is not found.", reportId));
  }
}

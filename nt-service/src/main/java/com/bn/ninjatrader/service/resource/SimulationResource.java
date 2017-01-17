package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.common.data.Report;
import com.bn.ninjatrader.model.dao.ReportDao;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.Clock;
import java.util.Optional;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
@Path("/simulation")
@Produces(MediaType.APPLICATION_JSON)
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
  public SimulationReport getReport(@PathParam("reportId") final String reportId) {
    final Optional<Report> foundReport = reportDao.findByReportId(reportId);

    if (foundReport.isPresent()) {
      return om.convertValue(foundReport.get().getData(), SimulationReport.class);
    }

    return null;
  }
}

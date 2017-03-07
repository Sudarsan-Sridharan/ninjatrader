package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.simulation.scanner.ScanResult;
import com.bn.ninjatrader.simulation.scanner.StockScanner;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
@Path("/task/scanner")
public class RunStockScannerTask {
  private static final Logger LOG = LoggerFactory.getLogger(RunStockScannerTask.class);

  private final StockScanner stockScanner;

  @Inject
  public RunStockScannerTask(final StockScanner stockScanner) {
    this.stockScanner = stockScanner;
  }

  @GET
  @Path("/run")
  @Produces(MediaType.APPLICATION_JSON)
  public Response runScanner(@QueryParam("algoId") final String algoId) {
    if (StringUtils.isEmpty(algoId)) {
      throw new BadRequestException("algoId parameter is required.");
    }

    final List<ScanResult> scanResults = stockScanner.scan(algoId);
    return Response.ok(scanResults).build();
  }
}

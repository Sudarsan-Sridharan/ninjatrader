package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.simulation.scanner.ScanRequest;
import com.bn.ninjatrader.simulation.scanner.ScanResult;
import com.bn.ninjatrader.simulation.scanner.StockScanner;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
@Deprecated //TODO NO NEED FOR THIS? Maybe use scanner worker.
@Path("/tasks/scanner")
public class RunStockScannerTask {
  private static final Logger LOG = LoggerFactory.getLogger(RunStockScannerTask.class);
  private static final int DEFAULT_DAYS = 1;

  private final StockScanner stockScanner;

  @Inject
  public RunStockScannerTask(final StockScanner stockScanner) {
    this.stockScanner = stockScanner;
  }

  @GET
  @Path("/run")
  @Produces(MediaType.APPLICATION_JSON)
  public Response runScanner(@QueryParam("algoId") final String algoId,
                             @QueryParam("days") final int days) {
    if (StringUtils.isEmpty(algoId)) {
      throw new BadRequestException("algoId parameter is required.");
    }

    final int numOfDays = days == 0 ? DEFAULT_DAYS : days;

    final List<ScanResult> scanResults = stockScanner.scan(ScanRequest.withAlgoId(algoId).days(numOfDays))
        .values()
        .stream()
        .sorted(Comparator.comparing(ScanResult::getSymbol))
        .collect(Collectors.toList());

    return Response.ok(scanResults).build();
  }
}

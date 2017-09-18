package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.service.store.ScanResultStore;
import com.bn.ninjatrader.simulation.scanner.ScanRequest;
import com.bn.ninjatrader.simulation.scanner.ScanResult;
import com.bn.ninjatrader.simulation.scanner.StockScanner;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/tasks/scan")
public class RunStockScannerTask {
  private static final Logger LOG = LoggerFactory.getLogger(RunStockScannerTask.class);

  private final StockScanner stockScanner;
  private final ScanResultStore scanResultStore;

  @Inject
  public RunStockScannerTask(final StockScanner stockScanner,
                             final ScanResultStore scanResultStore) {
    this.stockScanner = stockScanner;
    this.scanResultStore = scanResultStore;
  }

  @POST
  public Response runScanner(final ScanRequest req) {
    LOG.info("Received scan request for algorithmId: {}", req.getAlgorithmId());
    final Map<String, ScanResult> scanResult = stockScanner.scan(req);
    scanResultStore.merge(req.getAlgorithmId(), scanResult);
    return Response.ok(scanResult).build();
  }
}

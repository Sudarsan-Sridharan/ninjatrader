package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.model.dao.AlgorithmDao;
import com.bn.ninjatrader.push.PushPublisher;
import com.bn.ninjatrader.service.push.PushEvents;
import com.bn.ninjatrader.service.push.ScanUpdatePushMessage;
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
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

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
  private final PushPublisher pushPublisher;
  private final AlgorithmDao algorithmDao;
  private final Clock clock;

  @Inject
  public RunStockScannerTask(final StockScanner stockScanner,
                             final ScanResultStore scanResultStore,
                             final PushPublisher pushPublisher,
                             final AlgorithmDao algorithmDao,
                             final Clock clock) {
    this.stockScanner = stockScanner;
    this.scanResultStore = scanResultStore;
    this.pushPublisher = pushPublisher;
    this.algorithmDao = algorithmDao;
    this.clock = clock;
  }

  @POST
  public Response runScanner(final ScanRequest req) {
    final Map<String, ScanResult> scanResults = stockScanner.scan(req);

    if (scanResults.isEmpty()) {
      return Response.ok().build();
    }

    final String userId = findUserId(req);

    // Merge results and store
    scanResultStore.merge(req.getAlgorithmId(), scanResults);

    // Push only latest results.
    final LocalDate now = LocalDate.now(clock);
    final List<ScanResult> pushResults = scanResults.values().stream()
        .filter(scanResult -> scanResult.getLastTransaction().getDate().plusDays(req.getDays()).isAfter(now))
        .sorted(comparing(ScanResult::getSymbol))
        .collect(Collectors.toList());

    if (!pushResults.isEmpty()) {
      pushPublisher.push(userId, PushEvents.SCAN_UPDATE, new ScanUpdatePushMessage(req.getAlgorithmId(), pushResults));
    }

    return Response.ok().build();
  }

  private String findUserId(final ScanRequest req) {
    return algorithmDao.findOneByAlgorithmId(req.getAlgorithmId())
            .orElseThrow(() -> new IllegalStateException("Algorithm ID not found: " + req.getAlgorithmId()))
        .getUserId();
  }
}

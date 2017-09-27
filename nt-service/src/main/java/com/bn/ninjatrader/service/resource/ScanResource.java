package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.queue.Task;
import com.bn.ninjatrader.queue.TaskDispatcher;
import com.bn.ninjatrader.service.annotation.Secured;
import com.bn.ninjatrader.service.store.ScanResultStore;
import com.bn.ninjatrader.simulation.scanner.ScanRequest;
import com.bn.ninjatrader.simulation.scanner.ScanResult;
import com.google.common.collect.Maps;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
@Secured
@Path("/scan")
@Produces(MediaType.APPLICATION_JSON)
public class ScanResource {
  private static final Logger LOG = LoggerFactory.getLogger(ScanResource.class);

  private final ScanResultStore scanResultStore;
  private final TaskDispatcher taskDispatcher;
  private final Clock clock;

  @Inject
  public ScanResource(final ScanResultStore scanResultStore,
                      final TaskDispatcher taskDispatcher,
                      final Clock clock) {
    this.scanResultStore = scanResultStore;
    this.taskDispatcher = taskDispatcher;
    this.clock = clock;
  }

  /**
   * Get ScanResults for given algorithmId and filtered by days.
   * Results can either come from cache, or if not found, an asynchronous task will be triggered to do a full scan,
   * then results will be pushed to client.
   */
  @GET
  @Path("/{algorithmId}")
  public Response getScanResult(@PathParam("algorithmId") final String algorithmId,
                                @QueryParam("days") @DefaultValue("1") final int days) {

    final Optional<List<ScanResult>> cachedResults = findCachedScanResults(algorithmId, days);

    if (cachedResults.isPresent()) {
      return Response.ok(cachedResults.get()).build();
    } else {
      doAsyncScan(algorithmId, days);
      return Response.ok(Collections.emptyList()).build();
    }
  }

  /**
   * Find ScanResults from cache.
   */
  private Optional<List<ScanResult>> findCachedScanResults(final String algorithmId, final int days) {
    final Optional<Map<String, ScanResult>> foundResult = scanResultStore.get(algorithmId);

    if (!foundResult.isPresent()) {
      return Optional.empty();
    }

    final LocalDate now = LocalDate.now(clock);

    // Filter results by date of last transaction.
    final List<ScanResult> filteredResult = foundResult.get().values().stream()
        .filter(scanResult -> scanResult.getLastTransaction().getDate().plusDays(days).isAfter(now))
        .sorted(comparing(ScanResult::getSymbol))
        .collect(Collectors.toList());

    return Optional.of(filteredResult);
  }

  /**
   * Do an asynchronous scan. Results will be pushed to client once done.
   */
  private void doAsyncScan(final String algorithmId, final int days) {
    // Put empty mutable map in cache to prevent triggering duplicate full calcs.
    // Just return empty result and let client wait for the calc to finish.
    // IMPORTANT: Do not put Collections.emptyMap() as this is immutable!
    scanResultStore.put(algorithmId, Maps.newHashMap());

    // If results are not found, dispatch task to do full scan. Results will be pushed to client and cache.
    taskDispatcher.submitTask(Task.withPath("/tasks/scan").payload(ScanRequest.withAlgoId(algorithmId).days(days)));
  }
}
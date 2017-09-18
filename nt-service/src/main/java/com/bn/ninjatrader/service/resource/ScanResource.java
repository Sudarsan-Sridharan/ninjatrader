package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.service.annotation.Secured;
import com.bn.ninjatrader.service.store.ScanResultStore;
import com.bn.ninjatrader.simulation.scanner.ScanResult;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
  private static final int DEFAULT_TIMEOUT_SECONDS = 15;

  private final ScanResultStore scanResultStore;
  private final Clock clock;

  @Inject
  public ScanResource(final ScanResultStore scanResultStore, final Clock clock) {
    this.scanResultStore = scanResultStore;
    this.clock = clock;
  }

  @GET
  @Path("/{algorithmId}")
  public Response getScanResult(@PathParam("algorithmId") final String algorithmId,
                                @QueryParam("days") @DefaultValue("1") final int days)
      throws InterruptedException, ExecutionException, TimeoutException {

    final LocalDate now = LocalDate.now(clock);

    // Scan using given algorithm. Allowed to run only up to 10 seconds.
    final Map<String, ScanResult> result = scanResultStore.getOrCreate(algorithmId)
        .get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);

    // Filter results by date of last transaction.
    final List<ScanResult> filteredResult = result.values().stream()
        .filter(scanResult -> scanResult.getLastTransaction().getDate().plusDays(days).isAfter(now))
        .sorted(comparing(ScanResult::getSymbol))
        .collect(Collectors.toList());

    return Response.ok(filteredResult).build();
  }
}
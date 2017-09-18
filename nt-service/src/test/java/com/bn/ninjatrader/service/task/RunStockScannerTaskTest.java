package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.service.store.ScanResultStore;
import com.bn.ninjatrader.simulation.scanner.ScanRequest;
import com.bn.ninjatrader.simulation.scanner.ScanResult;
import com.bn.ninjatrader.simulation.scanner.StockScanner;
import com.google.common.collect.ImmutableMap;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.Map;

import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class RunStockScannerTaskTest extends JerseyTest {
  private static final Logger LOG = LoggerFactory.getLogger(RunStockScannerTaskTest.class);

  private StockScanner stockScanner;
  private ScanResultStore scanResultStore;

  @Override
  protected Application configure() {
    stockScanner = mock(StockScanner.class);
    scanResultStore = mock(ScanResultStore.class);

    final RunStockScannerTask task = new RunStockScannerTask(stockScanner, scanResultStore);
    return new ResourceConfig().register(task);
  }

  @Test
  public void testRunScanner_shouldScanAndStoreResults() {
    final Map<String, ScanResult> expectedScanResults = ImmutableMap.<String, ScanResult>builder()
        .put("MEG", ScanResult.builder().symbol("MEG").profit(10000).build())
        .put("BDO", ScanResult.builder().symbol("BDO").profit(-500).build())
        .build();

    when(stockScanner.scan(any())).thenReturn(expectedScanResults);

    // Send request
    final ScanRequest req = ScanRequest.withAlgoId("test_algo_id").days(3).symbols("MEG, BDO");
    final Response response = target("/tasks/scan").request().post(Entity.json(req));

    // Assert that response code is 200
    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

    // Verify results returned
    final  Map<String, ScanResult> actualScanResults =
        response.readEntity(new GenericType<Map<String, ScanResult>>() {});
    assertThat(actualScanResults).isEqualTo(expectedScanResults);

    // Verify results are stored
    verify(scanResultStore).merge("test_algo_id", expectedScanResults);
  }
}

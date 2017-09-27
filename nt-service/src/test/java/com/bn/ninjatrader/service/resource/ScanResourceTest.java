package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.queue.Task;
import com.bn.ninjatrader.queue.TaskDispatcher;
import com.bn.ninjatrader.service.store.ScanResultStore;
import com.bn.ninjatrader.simulation.scanner.ScanRequest;
import com.bn.ninjatrader.simulation.scanner.ScanResult;
import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import com.google.common.collect.ImmutableMap;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class ScanResourceTest extends JerseyTest {
  private static final LocalDateTime now = LocalDateTime.of(2017, 2, 1, 12, 30);

  private ScanResultStore scanResultStore;
  private TaskDispatcher taskDispatcher;

  @Override
  protected Application configure() {
    scanResultStore = mock(ScanResultStore.class);
    taskDispatcher = mock(TaskDispatcher.class);


    final Clock clock = Clock.fixed(now.toInstant(ZoneOffset.UTC), ZoneOffset.systemDefault());

    return new ResourceConfig().register(new ScanResource(scanResultStore, taskDispatcher, clock));
  }

  @Test
  public void testGetWithExistingResultInCache_shouldReturnCachedResult() {
    final Transaction txn = BuyTransaction.builder().date(now.toLocalDate()).shares(1000).build();
    final ScanResult scanResult = ScanResult.builder().lastTransaction(txn).symbol("MEG").build();

    when(scanResultStore.get(anyString())).thenReturn(Optional.of(ImmutableMap.of("MEG", scanResult)));

    final Response response = target("/scan/algo_id").request().get();

    // Verify status code
    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

    final List<ScanResult> results = response.readEntity(new GenericType<List<ScanResult>>() {});

    assertThat(results).containsExactly(scanResult);
  }

  @Test
  public void testGetWithDayQueryParam_shouldFilterResultsByLastTxnDate() {
    final Transaction megTxn = BuyTransaction.builder().date(now.toLocalDate()).shares(1000).build();
    final ScanResult megScanResult = ScanResult.builder().lastTransaction(megTxn).symbol("MEG").build();

    final Transaction bdoTxn = BuyTransaction.builder().date(now.toLocalDate().minusDays(1)).shares(1000).build();
    final ScanResult bdoScanResult = ScanResult.builder().lastTransaction(bdoTxn).symbol("BDO").build();

    when(scanResultStore.get(anyString()))
        .thenReturn(Optional.of(ImmutableMap.of("MEG", megScanResult, "BDO", bdoScanResult)));

    // Verify bdo is filtered out
    final Response response1 = target("/scan/algo_id").queryParam("days", 1).request().get();
    assertThat(response1.readEntity(new GenericType<List<ScanResult>>() {})).containsExactly(megScanResult);

    // Verify bdo is included and results are sorted by symbol
    final Response response2 = target("/scan/algo_id").queryParam("days", 2).request().get();
    assertThat(response2.readEntity(new GenericType<List<ScanResult>>() {}))
        .containsExactly(bdoScanResult, megScanResult);
  }

  @Test
  public void testGetWithNoResultInCache_shouldSendScanTask() {
    final ArgumentCaptor<Task<ScanRequest>> taskCaptor = ArgumentCaptor.forClass(Task.class);

    when(scanResultStore.get(anyString())).thenReturn(Optional.empty());

    final Response response = target("/scan/algo_id").request().get();

    // Verify status code
    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

    // Assert that results are empty
    final List<ScanResult> results = response.readEntity(new GenericType<List<ScanResult>>() {});
    assertThat(results).isEmpty();

    // Verify task is dispatched
    verify(taskDispatcher).submitTask(taskCaptor.capture());

    assertThat(taskCaptor.getValue().getPath()).isEqualTo("/tasks/scan");
    assertThat(taskCaptor.getValue().getPayload()).isNotNull();
    assertThat(taskCaptor.getValue().getPayload().getAlgorithmId()).isEqualTo("algo_id");
    assertThat(taskCaptor.getValue().getPayload().getDays()).isEqualTo(1);
  }
}

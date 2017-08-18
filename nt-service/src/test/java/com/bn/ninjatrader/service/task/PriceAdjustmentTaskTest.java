package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.process.adjustment.PriceAdjustmentRequest;
import com.bn.ninjatrader.process.adjustment.PriceAdjustmentService;
import com.bn.ninjatrader.simulation.core.SimulationRequest;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author bradwee2000@gmail.com
 */
public class PriceAdjustmentTaskTest extends JerseyTest {
  private static final Logger LOG = LoggerFactory.getLogger(PriceAdjustmentTaskTest.class);

  private static final LocalDate now = LocalDate.of(2016, 2, 1);
  private static final LocalDate tomorrow = now.plusDays(1);
  private static final String REST_URL = "/tasks/price-adjustment/run";

  @Captor
  private ArgumentCaptor<SimulationRequest> requestCaptor;

  private PriceAdjustmentService priceAdjustmentService;

  @Override
  protected Application configure() {
    priceAdjustmentService = mock(PriceAdjustmentService.class);
    final PriceAdjustmentTask task = new PriceAdjustmentTask(priceAdjustmentService);
    return new ResourceConfig().register(task).packages(DateTimeParseException.class.getPackage().getName());
  }

  @Before
  public void before() {
    initMocks(this);
    reset(priceAdjustmentService);
  }

  @Test
  public void testRun_shouldCallPriceAdjustmentService() {
    final PriceAdjustmentRequest request = PriceAdjustmentRequest.builder()
        .symbol("MEG").from(now).to(tomorrow).script("script").build();

    final Response response = target(REST_URL)
        .request()
        .post(Entity.json(request));

    verify(priceAdjustmentService).adjustPrices(request);

    assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
  }

  @Test
  public void testRunWithMissingSymbol_shouldReturnBadRequest() {
    final PriceAdjustmentRequest request = PriceAdjustmentRequest.builder().from(now).to(tomorrow).build();

    final Response response = target(REST_URL)
        .request()
        .post(Entity.json(request));

    assertThat(response.getStatus()).isEqualTo(BAD_REQUEST.getStatusCode());
    assertThat(response.readEntity(String.class)).isEqualTo(PriceAdjustmentTask.ERROR_SYM_PARAM_REQUIRED);
  }

  @Test
  public void testRunWithMissingScript_shouldReturnBadRequest() {
    final PriceAdjustmentRequest request = PriceAdjustmentRequest.builder()
        .symbol("MEG").from(now).to(tomorrow).build();

    final Response response = target(REST_URL)
        .request()
        .post(Entity.json(request));

    assertThat(response.getStatus()).isEqualTo(BAD_REQUEST.getStatusCode());
    assertThat(response.readEntity(String.class)).isEqualTo(PriceAdjustmentTask.ERROR_SCRIPT_PARAM_REQUIRED);
  }

  @Test
  public void testRunWithMissingFromDate_shouldReturnBadRequest() {
    final PriceAdjustmentRequest request = PriceAdjustmentRequest.builder()
        .symbol("MEG").to(tomorrow).script("script").build();

    final Response response = target("/tasks/price-adjustment/run")
        .request()
        .post(Entity.json(request));

    assertThat(response.getStatus()).isEqualTo(BAD_REQUEST.getStatusCode());
    assertThat(response.readEntity(String.class)).isEqualTo(PriceAdjustmentTask.ERROR_FROM_PARAM_REQUIRED);
  }

  @Test
  public void testRunWithMissingToDate_shouldReturnBadRequest() {
    final PriceAdjustmentRequest request = PriceAdjustmentRequest.builder()
        .symbol("MEG").from(now).script("script").build();

    final Response response = target(REST_URL)
        .request()
        .post(Entity.json(request));

    assertThat(response.getStatus()).isEqualTo(BAD_REQUEST.getStatusCode());
    assertThat(response.readEntity(String.class)).isEqualTo(PriceAdjustmentTask.ERROR_TO_PARAM_REQUIRED);
  }
}

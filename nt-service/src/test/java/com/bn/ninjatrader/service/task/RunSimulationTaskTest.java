package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.simulation.Simulator;
import com.bn.ninjatrader.simulation.core.SimulationRequest;
import com.bn.ninjatrader.simulation.model.TradeStatistic;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author bradwee2000@gmail.com
 */
public class RunSimulationTaskTest extends JerseyTest {
  private static final Logger LOG = LoggerFactory.getLogger(RunSimulationTaskTest.class);

  private static final Simulator simulator = mock(Simulator.class);

  @Captor
  private ArgumentCaptor<SimulationRequest> requestCaptor;

  private SimulationReport simulationReport;

  @Override
  protected Application configure() {
    final RunSimulationTask task = new RunSimulationTask(simulator);
    return new ResourceConfig().register(task);
  }

  @Before
  public void before() {
    reset(simulator);

    initMocks(this);

    simulationReport = SimulationReport.builder()
        .startingCash(100000)
        .endingCash(200000)
        .tradeStatistics(new TradeStatistic())
        .build();

    when(simulator.play(any(SimulationRequest.class))).thenReturn(simulationReport);
  }

  @Test
  public void testRun_shouldReturnSimulationReport() {
    final SimulationReport report = target("/task/simulation/run")
        .queryParam("symbol", "MEG")
        .queryParam("algoId", "algoId")
        .request()
        .get(SimulationReport.class);

    assertThat(report).isNotNull();
    assertThat(report.getStartingCash()).isEqualTo(100000);
    assertThat(report.getEndingCash()).isEqualTo(200000);
  }

  @Test
  public void testRunWithParams_shouldUseGivenParams() {
    target("/task/simulation/run")
        .queryParam("symbol", "MEG")
        .queryParam("from", "20160101")
        .queryParam("to", "20161231")
        .queryParam("algoId", "dtRKje03")
        .request().get();

    verify(simulator).play(requestCaptor.capture());

    final SimulationRequest req = requestCaptor.getValue();
    assertThat(req.getSymbol()).isEqualTo("MEG");
    assertThat(req.getFrom()).isEqualTo(LocalDate.of(2016, 1, 1));
    assertThat(req.getTo()).isEqualTo(LocalDate.of(2016, 12, 31));
    assertThat(req.getTradeAlgorithmId()).isEqualTo("dtRKje03");
  }

  @Test
  public void testRunWithNoInputSymbol_shouldReturn400Error() {
    final Response response = target("/task/simulation/run").request().get();
    assertThat(response.getStatus()).isEqualTo(400);
  }

  @Test
  public void testRunWithNoInputAlgoId_shouldReturn400Error() {
    final Response response = target("/task/simulation/run")
        .queryParam("symbol", "MEG")
        .request().get();
    assertThat(response.getStatus()).isEqualTo(400);
  }
}

package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.model.util.TestUtil;
import com.bn.ninjatrader.simulation.Simulator;
import com.bn.ninjatrader.simulation.core.SimulationParams;
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
import java.time.Clock;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author bradwee2000@gmail.com
 */
public class RunSimulationTaskTest extends JerseyTest {
  private static final Logger LOG = LoggerFactory.getLogger(RunSimulationTaskTest.class);

  private static final LocalDate now = LocalDate.of(2016, 2, 10);
  private static final Clock clock = TestUtil.fixedClock(now);
  private static final Simulator simulator = mock(Simulator.class);

  @Captor
  private ArgumentCaptor<SimulationParams> paramsCaptor;

  private SimulationReport simulationReport;

  @Override
  protected Application configure() {
    final RunSimulationTask task = new RunSimulationTask(simulator, clock);
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

    when(simulator.play(any(SimulationParams.class))).thenReturn(simulationReport);
  }

  @Test
  public void testRun_shouldReturnSimulationReport() {
    final SimulationReport report = target("/task/simulation/run")
        .queryParam("symbol", "MEG")
        .request()
        .get(SimulationReport.class);

    assertThat(report).isNotNull();
    assertThat(report.getStartingCash()).isEqualTo(100000);
    assertThat(report.getEndingCash()).isEqualTo(200000);
  }

  @Test
  public void testRunWithNoInputDate_shouldRunProcessWith2YearsData() {
    target("/task/simulation/run")
        .queryParam("symbol", "MEG")
        .request().get();

    verify(simulator).play(paramsCaptor.capture());

    final SimulationParams params = paramsCaptor.getValue();

    assertThat(params.getSymbol()).isEqualTo("MEG");
    assertThat(params.getFromDate()).isEqualTo(now.minusYears(2));
    assertThat(params.getToDate()).isEqualTo(now);
  }

  @Test
  public void testRunWithNoSymbolParam_shouldReturnBadRequestError() {
    final Response response = target("/task/simulation/run").request().get();
    assertThat(response.getStatus()).isEqualTo(400);
  }
}

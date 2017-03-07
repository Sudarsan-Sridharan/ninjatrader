package com.bn.ninjatrader.simulation;

import com.bn.ninjatrader.model.dao.TradeAlgorithmDao;
import com.bn.ninjatrader.model.entity.TradeAlgorithm;
import com.bn.ninjatrader.model.request.FindTradeAlgorithmRequest;
import com.bn.ninjatrader.model.util.TestUtil;
import com.bn.ninjatrader.simulation.core.Simulation;
import com.bn.ninjatrader.simulation.core.SimulationFactory;
import com.bn.ninjatrader.simulation.core.SimulationParams;
import com.bn.ninjatrader.simulation.core.SimulationRequest;
import com.bn.ninjatrader.simulation.jackson.SimObjectMapperProvider;
import com.bn.ninjatrader.simulation.model.SimTradeAlgorithm;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author bradwee2000@gmail.com
 */
public class SimulatorTest {

  private final LocalDate now = LocalDate.of(2016, 2, 1);
  private final LocalDate tomorrow = LocalDate.of(2016, 2, 2);
  private final Clock clock  = TestUtil.fixedClock(now);
  private final SimObjectMapperProvider omProvider = new SimObjectMapperProvider();

  @Captor
  private ArgumentCaptor<SimulationParams> paramsCaptor;

  private SimulationFactory simulationFactory;
  private Simulation simulation;
  private SimulationReport simulationReport;
  private TradeAlgorithmDao tradeAlgorithmDao;

  private Simulator simulator;

  @Before
  public void before() {
    initMocks(this);
    simulationFactory = mock(SimulationFactory.class);
    simulation = mock(Simulation.class);
    simulationReport = mock(SimulationReport.class);
    tradeAlgorithmDao = mock(TradeAlgorithmDao.class);

    simulator = new Simulator(simulationFactory, tradeAlgorithmDao, clock, omProvider);

    when(simulationFactory.create(any(SimulationParams.class))).thenReturn(simulation);
    when(simulation.play()).thenReturn(simulationReport);
  }

  @Test
  public void testPlayWithSimParams_shouldReturnSimulationReport() {
    assertThat(simulator.play(mock(SimulationParams.class))).isEqualTo(simulationReport);
  }

  @Test
  public void testPlayWithRequestParams_shouldCreateSimParamsBasedOnRequestParams()
      throws JsonProcessingException {
    final SimTradeAlgorithm algo = SimTradeAlgorithm.builder().build();
    final String algoJson = omProvider.get().writeValueAsString(algo);

    when(tradeAlgorithmDao.findOne(any(FindTradeAlgorithmRequest.class)))
        .thenReturn(Optional.of(TradeAlgorithm.builder().id("ID").userId("TESTER").algorithm(algoJson).build()));

    // Play
    simulator.play(SimulationRequest.withSymbol("MEG").from(now).to(tomorrow).tradeAlgorithmId("algoId"));

    // Verify SimulationParams created
    verify(simulationFactory).create(paramsCaptor.capture());

    // Verify SimulaitonParams
    final SimulationParams params = paramsCaptor.getValue();
    assertThat(params.getSymbol()).isEqualTo("MEG");
    assertThat(params.getFromDate()).isEqualTo(now);
    assertThat(params.getToDate()).isEqualTo(tomorrow);
    assertThat(params.getAlgorithm()).isEqualTo(algo);
  }
}

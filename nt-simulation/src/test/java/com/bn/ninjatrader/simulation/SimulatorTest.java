package com.bn.ninjatrader.simulation;

import com.bn.ninjatrader.simulation.core.Simulation;
import com.bn.ninjatrader.simulation.core.SimulationFactory;
import com.bn.ninjatrader.simulation.core.SimulationParams;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class SimulatorTest {

  private SimulationFactory simulationFactory;
  private Simulation simulation;
  private SimulationReport simulationReport;

  private Simulator simulator;

  @Before
  public void before() {
    simulationFactory = mock(SimulationFactory.class);
    simulation = mock(Simulation.class);
    simulationReport = mock(SimulationReport.class);

    simulator = new Simulator(simulationFactory);

    when(simulationFactory.create(any(SimulationParams.class))).thenReturn(simulation);
    when(simulation.play()).thenReturn(simulationReport);
  }

  @Test
  public void testPlay_shouldReturnSimulationReport() {
    assertThat(simulator.play(mock(SimulationParams.class))).isEqualTo(simulationReport);
  }
}

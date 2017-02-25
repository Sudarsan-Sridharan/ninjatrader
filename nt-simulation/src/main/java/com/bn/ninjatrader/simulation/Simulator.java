package com.bn.ninjatrader.simulation;

import com.bn.ninjatrader.simulation.core.Simulation;
import com.bn.ninjatrader.simulation.core.SimulationFactory;
import com.bn.ninjatrader.simulation.core.SimulationParams;
import com.bn.ninjatrader.simulation.printer.SimulationReportPrinter;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Brad on 8/3/16.
 */
@Singleton
public class Simulator {
  private static final Logger LOG = LoggerFactory.getLogger(Simulator.class);

  private SimulationFactory simulationFactory;

  @Inject
  public Simulator(final SimulationFactory simulationFactory) {
    this.simulationFactory = simulationFactory;
  }

  public SimulationReport play(final SimulationParams params) {
    final Simulation simulation = simulationFactory.create(params);
    SimulationReport simulationReport = simulation.play();
    return simulationReport;
  }

  private static void permute(final Simulator simulator) {
    SimulationReport biggestGain = null;

    for (double pullbackSensitivity = 0.005; pullbackSensitivity < 0.05; pullbackSensitivity += 0.005) {
      for (int buyPipsBuffer=1; buyPipsBuffer < 10; buyPipsBuffer++) {
        for (int sellPipsBuffer=1; sellPipsBuffer < 10; sellPipsBuffer++) {
          final SimulationReport report1 = simulator.play(TradeAlgorithm.newInstance()
              .pullbackSensitivity(pullbackSensitivity)
              .buyPipsBuffer(2)
              .sellPipsBuffer(sellPipsBuffer)
              .forSymbol("ALCO")
              .build());
          if (biggestGain == null) {
            LOG.info("INIT: pullbackSensitivity: {}  buyPipsBuffer: {}  sellPipsBuffer: {}",
                pullbackSensitivity, buyPipsBuffer, sellPipsBuffer);
            biggestGain = report1;
          } else if (biggestGain.getEndingCash() < report1.getEndingCash()) {
            LOG.info("BIGGER GAIN: pullbackSensitivity: {}  buyPipsBuffer: {}  sellPipsBuffer: {}",
                pullbackSensitivity, buyPipsBuffer, sellPipsBuffer);
            biggestGain = report1;
          }
        }
      }
    }
    new SimulationReportPrinter().printReport(biggestGain);
  }
}

package com.bn.ninjatrader.simulation;

import com.bn.ninjatrader.calculator.guice.NtCalculatorModule;
import com.bn.ninjatrader.common.data.Report;
import com.bn.ninjatrader.model.dao.ReportDao;
import com.bn.ninjatrader.model.guice.NtModelModule;
import com.bn.ninjatrader.simulation.core.Simulation;
import com.bn.ninjatrader.simulation.core.SimulationFactory;
import com.bn.ninjatrader.simulation.core.SimulationParams;
import com.bn.ninjatrader.simulation.guice.NtSimulationModule;
import com.bn.ninjatrader.simulation.printer.SimulationReportPrinter;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

/**
 * Created by Brad on 8/3/16.
 */
@Singleton
public class Simulator {
  private static final Logger LOG = LoggerFactory.getLogger(Simulator.class);

  private SimulationFactory simulationFactory;
  private ReportDao reportDao;

  @Inject
  public Simulator(final SimulationFactory simulationFactory,
                   final ReportDao reportDao) {
    this.simulationFactory = simulationFactory;
    this.reportDao = reportDao;
  }

  public SimulationReport play(final SimulationParams params) {
    final Simulation simulation = simulationFactory.create(params);
    SimulationReport simulationReport = simulation.play();
    return simulationReport;
  }

  public void saveReport(final SimulationReport simulationReport) {
    final Report report = new Report();
    report.setReportId("SAMPLE_REPORT"); // TODO REMOVE THIS!!
    report.setData(simulationReport);
    reportDao.save(report);
  }

  public static void main(final String args[]) {
    final Injector injector = Guice.createInjector(
        new NtModelModule(),
        new NtCalculatorModule(),
        new NtSimulationModule()
    );

    final Simulator simulator = injector.getInstance(Simulator.class);
    final SimulationReport report = simulator.play(GoldenAlgo.newInstance()
        .from(LocalDate.now().minusYears(10))
        .to(LocalDate.now().minusYears(0))
//        .maxBuyRisk(0.10)
//        .pullbackSensit ivity(0.005)
        .forSymbol("MCP"));
    simulator.saveReport(report);
    new SimulationReportPrinter().printReport(report);
//    permute(simulator);
  }

  private static void permute(final Simulator simulator) {
    SimulationReport biggestGain = null;

    for (double pullbackSensitivity = 0.005; pullbackSensitivity < 0.05; pullbackSensitivity += 0.005) {
      for (int buyPipsBuffer=1; buyPipsBuffer < 10; buyPipsBuffer++) {
        for (int sellPipsBuffer=1; sellPipsBuffer < 10; sellPipsBuffer++) {
          final SimulationReport report1 = simulator.play(GoldenAlgo.newInstance()
              .pullbackSensitivity(pullbackSensitivity)
              .buyPipsBuffer(2)
              .sellPipsBuffer(sellPipsBuffer)
              .forSymbol("ALCO"));
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

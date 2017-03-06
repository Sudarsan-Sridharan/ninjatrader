package com.bn.ninjatrader.service.dropwizard.runner;

import com.bn.ninjatrader.model.mongo.guice.NtModelMongoModule;
import com.bn.ninjatrader.simulation.Simulator;
import com.bn.ninjatrader.simulation.TradeAlgorithm;
import com.bn.ninjatrader.simulation.guice.NtSimulationModule;
import com.bn.ninjatrader.simulation.printer.SimulationReportPrinter;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDate;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class SimulationRunner {

  private final Simulator simulator;
  private final SimulationReportPrinter printer;

  @Inject
  public SimulationRunner(final Simulator simulator,
                          final SimulationReportPrinter printer) {
    this.simulator = simulator;
    this.printer = printer;
  }

  public void run() {
    final LocalDate to = LocalDate.now();
    final LocalDate from = to.minusYears(10);


    final SimulationReport report = simulator.play(TradeAlgorithm.newInstance()
        .from(from)
        .to(to)
        .maxBuyRisk(0.1)
        .forSymbol("ALCO")
        .build());

    printer.printReport(report);
  }

  public static void main(final String args[]) {
    final Injector injector = Guice.createInjector(
        new NtModelMongoModule(),
        new NtSimulationModule());

    final SimulationRunner runner = injector.getInstance(SimulationRunner.class);
    runner.run();
  }
}

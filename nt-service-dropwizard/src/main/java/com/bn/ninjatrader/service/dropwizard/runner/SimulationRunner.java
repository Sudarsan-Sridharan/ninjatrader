package com.bn.ninjatrader.service.dropwizard.runner;

import com.bn.ninjatrader.model.dao.TradeAlgorithmDao;
import com.bn.ninjatrader.model.mongo.guice.NtModelMongoModule;
import com.bn.ninjatrader.simulation.GoldenAlgorithm;
import com.bn.ninjatrader.simulation.Simulator;
import com.bn.ninjatrader.simulation.core.SimulationParams;
import com.bn.ninjatrader.simulation.guice.NtSimulationModule;
import com.bn.ninjatrader.simulation.model.SimTradeAlgorithm;
import com.bn.ninjatrader.simulation.printer.SimulationReportPrinter;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.bn.ninjatrader.simulation.service.SaveSimTradeAlgoRequest;
import com.bn.ninjatrader.simulation.service.SimTradeAlgorithmService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDate;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class SimulationRunner {
  private static final Logger LOG = LoggerFactory.getLogger(SimulationRunner.class);

  private final Simulator simulator;
  private final SimulationReportPrinter printer;
  private final TradeAlgorithmDao tradeAlgorithmDao;
  private final SimTradeAlgorithmService simTradeAlgorithmService;

  @Inject
  public SimulationRunner(final Simulator simulator,
                          final SimulationReportPrinter printer,
                          final TradeAlgorithmDao tradeAlgorithmDao,
                          final SimTradeAlgorithmService simTradeAlgorithmService) {
    this.simulator = simulator;
    this.printer = printer;
    this.tradeAlgorithmDao = tradeAlgorithmDao;
    this.simTradeAlgorithmService = simTradeAlgorithmService;
  }

  public void run() {
    final LocalDate to = LocalDate.now();
    final LocalDate from = to.minusYears(10);

    final SimulationParams params = GoldenAlgorithm.newInstance()
        .from(from)
        .to(to)
        .maxBuyRisk(0.05)
        .forSymbol("ALCO").build();

    final SimTradeAlgorithm algorithm = params.getAlgorithm();

    final SimulationReport report = simulator.play(params);

    printer.printReport(report);

    simTradeAlgorithmService.save(SaveSimTradeAlgoRequest.withAlgorithm(algorithm)
        .tradeAlgorithmId("ADMIN")
        .userId("ADMIN")
        .description("Secret Sauce"));
  }

  public static void main(final String args[]) {
    final Injector injector = Guice.createInjector(
        new NtModelMongoModule(),
        new NtSimulationModule());

    final SimulationRunner runner = injector.getInstance(SimulationRunner.class);
    runner.run();
  }
}

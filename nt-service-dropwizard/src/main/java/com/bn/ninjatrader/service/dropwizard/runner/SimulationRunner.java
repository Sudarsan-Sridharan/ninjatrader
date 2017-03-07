package com.bn.ninjatrader.service.dropwizard.runner;

import com.bn.ninjatrader.model.dao.TradeAlgorithmDao;
import com.bn.ninjatrader.model.entity.TradeAlgorithm;
import com.bn.ninjatrader.model.entity.TradeAlgorithmFactory;
import com.bn.ninjatrader.model.mongo.guice.NtModelMongoModule;
import com.bn.ninjatrader.model.request.SaveTradeAlgorithmRequest;
import com.bn.ninjatrader.service.provider.ObjectMapperContextResolver;
import com.bn.ninjatrader.simulation.Simulator;
import com.bn.ninjatrader.simulation.GoldenAlgorithm;
import com.bn.ninjatrader.simulation.core.SimulationParams;
import com.bn.ninjatrader.simulation.core.SimulationRequest;
import com.bn.ninjatrader.simulation.guice.NtSimulationModule;
import com.bn.ninjatrader.simulation.model.SimTradeAlgorithm;
import com.bn.ninjatrader.simulation.printer.SimulationReportPrinter;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
  private final TradeAlgorithmFactory tradeAlgorithmFactory;
  private final ObjectMapper om;

  @Inject
  public SimulationRunner(final Simulator simulator,
                          final SimulationReportPrinter printer,
                          final TradeAlgorithmDao tradeAlgorithmDao,
                          final TradeAlgorithmFactory tradeAlgorithmFactory,
                          final ObjectMapperContextResolver objectMapperContextResolver) {
    this.simulator = simulator;
    this.printer = printer;
    this.tradeAlgorithmDao = tradeAlgorithmDao;
    this.tradeAlgorithmFactory = tradeAlgorithmFactory;
    this.om = objectMapperContextResolver.get();
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

    try {
      final String jsonAlgo = om.writeValueAsString(algorithm);
      final TradeAlgorithm algo = TradeAlgorithm.builder()
          .id("ADMIN").userId("ADMIN").algorithm(jsonAlgo).build();
      tradeAlgorithmDao.save(SaveTradeAlgorithmRequest.addEntity(algo));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    simulator.play(SimulationRequest.withSymbol("MEG").tradeAlgorithmId("ADMIN"));
  }

  public static void main(final String args[]) {
    final Injector injector = Guice.createInjector(
        new NtModelMongoModule(),
        new NtSimulationModule());

    final SimulationRunner runner = injector.getInstance(SimulationRunner.class);
    runner.run();
  }
}

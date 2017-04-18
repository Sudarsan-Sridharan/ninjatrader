package com.bn.ninjatrader.service.dropwizard.runner;

import com.bn.ninjatrader.model.dao.AlgorithmDao;
import com.bn.ninjatrader.model.entity.TradeAlgorithm;
import com.bn.ninjatrader.model.mongo.guice.NtModelMongoModule;
import com.bn.ninjatrader.simulation.core.Simulation;
import com.bn.ninjatrader.simulation.core.SimulationFactory;
import com.bn.ninjatrader.simulation.core.SimulationRequest;
import com.bn.ninjatrader.simulation.guice.NtSimulationModule;
import com.bn.ninjatrader.simulation.printer.SimulationReportPrinter;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.bn.ninjatrader.simulation.script.AlgorithmScript;
import com.bn.ninjatrader.simulation.script.GroovyAlgorithmScript;
import com.bn.ninjatrader.simulation.service.AlgorithmService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class SimulationRunner {
  private static final Logger LOG = LoggerFactory.getLogger(SimulationRunner.class);

  private final SimulationFactory simulationFactory;
  private final SimulationReportPrinter printer;
  private final AlgorithmDao tradeAlgorithmDao;
  private final AlgorithmService algorithmService;

  @Inject
  public SimulationRunner(final SimulationFactory simulationFactory,
                          final SimulationReportPrinter printer,
                          final AlgorithmDao tradeAlgorithmDao,
                          final AlgorithmService algorithmService) {
    this.simulationFactory = simulationFactory;
    this.printer = printer;
    this.tradeAlgorithmDao = tradeAlgorithmDao;
    this.algorithmService = algorithmService;
  }

  public void run() throws IOException {
    final LocalDate to = LocalDate.now();
    final LocalDate from = to.minusYears(10);

    final String scriptText = IOUtils.toString(SimulationRunner.class.getResourceAsStream("/SecretSauce.groovy"), Charset.forName("UTF-8"));
    final AlgorithmScript algorithm = new GroovyAlgorithmScript(scriptText);

    final SimulationRequest req = SimulationRequest.withSymbol("MEG").from(from).to(to).algorithmScript(algorithm);

    final Simulation simulation = simulationFactory.create(req);

    final SimulationReport report = simulation.play();

    printer.printReport(report);



    tradeAlgorithmDao.save(TradeAlgorithm.builder().algorithm(scriptText)
        .userId("ADMIN")
        .algoId("RISKON")
        .description("10% Risk On!!")
        .build());

//    simTradeAlgorithmService.save(SaveSimTradeAlgoRequest.withScriptText(scriptText)
//        .tradeAlgorithmId("ADMIN")
//        .userId("ADMIN")
//        .description("Secret Sauce"));

//    simTradeAlgorithmService.save(SaveSimTradeAlgoRequest.withAlgorithm(algorithm)
//        .tradeAlgorithmId("testalgo")
//        .userId("ADMIN")
//        .description("Test Algo"));
  }

  public static void main(final String args[]) throws IOException {
    final Injector injector = Guice.createInjector(
        new NtModelMongoModule(),
        new NtSimulationModule());

    final SimulationRunner runner = injector.getInstance(SimulationRunner.class);
    runner.run();
  }
}

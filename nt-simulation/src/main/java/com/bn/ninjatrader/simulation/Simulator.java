package com.bn.ninjatrader.simulation;

import com.bn.ninjatrader.simulation.core.Simulation;
import com.bn.ninjatrader.simulation.core.SimulationFactory;
import com.bn.ninjatrader.simulation.core.SimulationParams;
import com.bn.ninjatrader.simulation.core.SimulationRequest;
import com.bn.ninjatrader.simulation.model.SimTradeAlgorithm;
import com.bn.ninjatrader.simulation.printer.SimulationReportPrinter;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.bn.ninjatrader.simulation.service.SimTradeAlgorithmService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 8/3/16.
 */
@Singleton
public class Simulator {
  private static final Logger LOG = LoggerFactory.getLogger(Simulator.class);

  private final SimulationFactory simulationFactory;
  private final SimTradeAlgorithmService simTradeAlgorithmService;
  private final Clock clock;

  @Inject
  public Simulator(final SimulationFactory simulationFactory,
                   final Clock clock,
                   final SimTradeAlgorithmService simTradeAlgorithmService) {
    this.simulationFactory = simulationFactory;
    this.simTradeAlgorithmService = simTradeAlgorithmService;
    this.clock = clock;
  }

  @Deprecated
  public SimulationReport play(final SimulationParams params) {
    final Simulation simulation = simulationFactory.create(params);
    SimulationReport simulationReport = simulation.play();
    return simulationReport;
  }

  public SimulationReport play(final SimulationRequest req) {
    checkNotNull(req, "SimulationRequest must not be null.");
    checkArgument(!StringUtils.isEmpty(req.getSymbol()), "symbol must not be empty.");
    checkArgument(!StringUtils.isEmpty(req.getTradeAlgorithmId()), "tradeAlgorithmId must not be empty.");

    // Prepare date range defaults if not given.
    final LocalDate from = Optional.ofNullable(req.getFrom()).orElse(LocalDate.now(clock).minusYears(1));
    final LocalDate to = Optional.ofNullable(req.getTo()).orElse(LocalDate.now(clock));

    // Prepare starting cash default if not given.
    final double startingCash = req.getStartingCash() == 0d ? 100_000.0 : req.getStartingCash();

    // Read algorithm from json
    final SimTradeAlgorithm algo = simTradeAlgorithmService.findById(req.getTradeAlgorithmId());

    // Build simulation parameters
    final SimulationParams params = SimulationParams.builder()
        .startingCash(startingCash)
        .from(from).to(to)
        .symbol(req.getSymbol())
        .algorithm(algo)
        .build();

    // Play and return result
    return play(params);
  }

  private static void permute(final Simulator simulator) {
    SimulationReport biggestGain = null;

    for (double pullbackSensitivity = 0.005; pullbackSensitivity < 0.05; pullbackSensitivity += 0.005) {
      for (int buyPipsBuffer=1; buyPipsBuffer < 10; buyPipsBuffer++) {
        for (int sellPipsBuffer=1; sellPipsBuffer < 10; sellPipsBuffer++) {
          final SimulationReport report1 = simulator.play(GoldenAlgorithm.newInstance()
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

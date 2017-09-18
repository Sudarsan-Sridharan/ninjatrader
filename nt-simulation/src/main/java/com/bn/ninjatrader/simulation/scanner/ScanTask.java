package com.bn.ninjatrader.simulation.scanner;

import com.bn.ninjatrader.simulation.core.Simulation;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * Task to run simulations in grid.
 */
public final class ScanTask implements Callable<Map<String, ScanResult>>, Serializable {
  private final List<Simulation> simulations;
  private final SimulationReportConverter simulationReportConverter;

  private ScanTask() {
    simulations = null;
    simulationReportConverter = null;
  }

  public ScanTask(final SimulationReportConverter simulationReportConverter,
                  final List<Simulation> simulations) {
    this.simulations = simulations;
    this.simulationReportConverter = simulationReportConverter;
  }

  @Override
  public Map<String, ScanResult> call() throws Exception {
    final List<SimulationReport> reports = this.simulations.stream()
        .map(simulation -> simulation.play())
        .collect(Collectors.toList());
    return simulationReportConverter.convert(reports);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("simulations", simulations)
        .toString();
  }
}

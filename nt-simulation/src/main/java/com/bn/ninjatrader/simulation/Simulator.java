package com.bn.ninjatrader.simulation;

import com.bn.ninjatrader.calculator.guice.NtCalculatorModule;
import com.bn.ninjatrader.common.data.Report;
import com.bn.ninjatrader.model.dao.ReportDao;
import com.bn.ninjatrader.model.guice.NtModelModule;
import com.bn.ninjatrader.simulation.condition.Conditions;
import com.bn.ninjatrader.simulation.core.Simulation;
import com.bn.ninjatrader.simulation.core.SimulationFactory;
import com.bn.ninjatrader.simulation.core.SimulationParams;
import com.bn.ninjatrader.simulation.operation.Variable;
import com.bn.ninjatrader.simulation.guice.NtSimulationModule;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static com.bn.ninjatrader.simulation.condition.Conditions.*;
import static com.bn.ninjatrader.simulation.data.DataType.PRICE_CLOSE;
import static com.bn.ninjatrader.simulation.data.DataType.RSI;
import static com.bn.ninjatrader.simulation.data.DataType.SMA;
import static com.bn.ninjatrader.simulation.operation.function.Functions.highestInLastNBars;
import static com.bn.ninjatrader.simulation.order.MarketTime.CLOSE;
import static com.bn.ninjatrader.simulation.order.OrderParameters.buy;
import static com.bn.ninjatrader.simulation.order.OrderParameters.sell;

/**
 * Created by Brad on 8/3/16.
 */
@Singleton
public class Simulator {
  private static final Logger LOG = LoggerFactory.getLogger(Simulator.class);

  @Inject
  private SimulationFactory simulationFactory;

  @Inject
  private ReportDao reportDao;

  public SimulationReport play(final SimulationParams params) {
    Simulation simulation = simulationFactory.create(params);
    SimulationReport simulationReport = simulation.play();

    saveReport(simulationReport);

    return simulationReport;
  }

  private void saveReport(SimulationReport simulationReport) {
    Report report = new Report();
    report.setReportId("SAMPLE_REPORT"); // TODO REMOVE THIS!!
    report.setData(simulationReport);

    reportDao.save(report);
  }

  public static void main(String args[]) {
    Injector injector = Guice.createInjector(
        new NtModelModule(),
        new NtCalculatorModule(),
        new NtSimulationModule()
    );
    Simulator simulator = injector.getInstance(Simulator.class);

    SimulationParams params = new SimulationParams();
    params.setSymbol("MEG");
    params.setFromDate(LocalDate.now().minusYears(10));
    params.setToDate(LocalDate.now());
    params.setStartingCash(100000);

    params.setBuyCondition(Conditions.create()
        .add(gt(PRICE_CLOSE, Variable.of(SMA).period(21)))
        .add(gt(Variable.of(SMA).period(21), Variable.of(SMA).period(50)))
        .add(gt(PRICE_CLOSE, highestInLastNBars(PRICE_CLOSE, 4)))
//        .add(gt(PRICE_CLOSE, barsAgo(PRICE_CLOSE, 100)))
    );

    params.setBuyOrderParams(buy()
        .at(CLOSE)
        .barsFromNow(0)
        .build());

    params.setSellCondition(Conditions.create()
        .add(Conditions.or(
            lt(PRICE_CLOSE, Variable.of(SMA).period(50)),
            gt(Variable.of(RSI).period(14), 80)
            ))
//        .add(lt(Operations.create(PRICE_CLOSE).mult(1.05), SMA_50))
    );

    params.setSellOrderParams(sell()
        .at(CLOSE)
        .barsFromNow(0)
        .build());

    simulator.play(params);
  }
}

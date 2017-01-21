package com.bn.ninjatrader.simulation;

import com.bn.ninjatrader.calculator.guice.NtCalculatorModule;
import com.bn.ninjatrader.common.data.Report;
import com.bn.ninjatrader.model.dao.ReportDao;
import com.bn.ninjatrader.model.guice.NtModelModule;
import com.bn.ninjatrader.simulation.condition.Conditions;
import com.bn.ninjatrader.simulation.core.Simulation;
import com.bn.ninjatrader.simulation.core.SimulationFactory;
import com.bn.ninjatrader.simulation.core.SimulationParams;
import com.bn.ninjatrader.simulation.guice.NtSimulationModule;
import com.bn.ninjatrader.simulation.operation.Variable;
import com.bn.ninjatrader.simulation.order.MarketTime;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.bn.ninjatrader.simulation.statement.BuyOrderStatement;
import com.bn.ninjatrader.simulation.statement.ConditionalStatment;
import com.bn.ninjatrader.simulation.statement.SellOrderStatement;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static com.bn.ninjatrader.simulation.condition.Conditions.gt;
import static com.bn.ninjatrader.simulation.condition.Conditions.lt;
import static com.bn.ninjatrader.simulation.data.DataType.EMA;
import static com.bn.ninjatrader.simulation.operation.Variables.PRICE_CLOSE;

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
    final Simulation simulation = simulationFactory.create(params);
    SimulationReport simulationReport = simulation.play();
    saveReport(simulationReport);
    return simulationReport;
  }

  private void saveReport(final SimulationReport simulationReport) {
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

    final SimulationParams params = SimulationParams.builder()
        .symbol("MEG")
        .from(LocalDate.now().minusYears(10))
        .to(LocalDate.now())
        .startingCash(100000)

        // Buy Condition
        .addStatement(ConditionalStatment.builder()
            .condition(Conditions.create()
                .add(gt(PRICE_CLOSE, Variable.of(EMA).period(18)))
                .add(gt(Variable.of(EMA).period(18), Variable.of(EMA).period(50)))
                .add(gt(Variable.of(EMA).period(50), Variable.of(EMA).period(100)))
                .add(gt(Variable.of(EMA).period(100), Variable.of(EMA).period(200))))
            .then(BuyOrderStatement.builder().marketTime(MarketTime.CLOSE).barsFromNow(1).build())
            .build())

        // Sell Condition
        .addStatement(ConditionalStatment.builder()
            .condition(Conditions.create()
                .add(lt(PRICE_CLOSE, Variable.of(EMA).period(18)))
                .add(lt(Variable.of(EMA).period(18), Variable.of(EMA).period(50))))
            .then(SellOrderStatement.builder().marketTime(MarketTime.CLOSE).barsFromNow(0).build())
            .build())
        .build();

    simulator.play(params);
  }
}

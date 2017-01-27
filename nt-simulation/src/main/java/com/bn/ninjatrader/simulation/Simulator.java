package com.bn.ninjatrader.simulation;

import com.bn.ninjatrader.calculator.guice.NtCalculatorModule;
import com.bn.ninjatrader.common.data.Report;
import com.bn.ninjatrader.logical.expression.condition.Conditions;
import com.bn.ninjatrader.model.dao.ReportDao;
import com.bn.ninjatrader.model.guice.NtModelModule;
import com.bn.ninjatrader.simulation.core.Simulation;
import com.bn.ninjatrader.simulation.core.SimulationFactory;
import com.bn.ninjatrader.simulation.core.SimulationParams;
import com.bn.ninjatrader.simulation.guice.NtSimulationModule;
import com.bn.ninjatrader.simulation.model.MarketTime;
import com.bn.ninjatrader.simulation.operation.function.HistoryValue;
import com.bn.ninjatrader.simulation.operation.function.LowestValue;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.bn.ninjatrader.simulation.statement.*;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static com.bn.ninjatrader.logical.expression.condition.Conditions.eq;
import static com.bn.ninjatrader.logical.expression.condition.Conditions.lt;
import static com.bn.ninjatrader.simulation.operation.Variables.*;

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
//        .addStatement(ConditionalStatement.builder()
//            .condition(Conditions.create()
//                .add(gt(PRICE_CLOSE, EMA.withPeriod(18)))
//                .add(gt(EMA.withPeriod(18), EMA.withPeriod(50)))
//                .add(gt(EMA.withPeriod(50), EMA.withPeriod(100)))
//                .add(gt(EMA.withPeriod(100), EMA.withPeriod(200))))
//            .then(BuyOrderStatement.builder().marketTime(MarketTime.CLOSE).barsFromNow(1).build())
//            .build())

        // Sell Condition
        .addStatement(ConditionalStatement.builder()
            .condition(Conditions.create()
//                .add(lt(PRICE_CLOSE, ))
                .add(lt(PRICE_CLOSE, EMA.withPeriod(18)))
                .add(lt(EMA.withPeriod(18), EMA.withPeriod(50))))
            .then(SellOrderStatement.builder().marketTime(MarketTime.CLOSE).barsFromNow(0).build())
            .build())

        // Pullback Condition
        .addStatement(ConditionalStatement.builder()
            .condition(Conditions.create()
                .add(eq(HistoryValue.of(PRICE_LOW).inNumOfBarsAgo(4), LowestValue.of(PRICE_LOW).inNumOfBarsAgo(15))))
            .then(MultiStatement.builder()
                .add(SetPropertyStatement.builder()
                    .add("LAST_PULLBACK", HistoryValue.of(PRICE_LOW).inNumOfBarsAgo(4))
                    .build())
                .add(BuyOrderStatement.builder().marketTime(MarketTime.CLOSE).barsFromNow(0).build())
                .build())
            .build()
        )


        .build();

    simulator.play(params);
  }
}

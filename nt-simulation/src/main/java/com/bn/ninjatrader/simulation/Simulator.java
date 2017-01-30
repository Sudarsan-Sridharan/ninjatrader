package com.bn.ninjatrader.simulation;

import com.bn.ninjatrader.calculator.guice.NtCalculatorModule;
import com.bn.ninjatrader.common.data.Report;
import com.bn.ninjatrader.logical.expression.condition.Conditions;
import com.bn.ninjatrader.logical.expression.operation.Operations;
import com.bn.ninjatrader.model.dao.ReportDao;
import com.bn.ninjatrader.model.guice.NtModelModule;
import com.bn.ninjatrader.simulation.core.Simulation;
import com.bn.ninjatrader.simulation.core.SimulationFactory;
import com.bn.ninjatrader.simulation.core.SimulationParams;
import com.bn.ninjatrader.simulation.guice.NtSimulationModule;
import com.bn.ninjatrader.simulation.operation.function.HistoryValue;
import com.bn.ninjatrader.simulation.operation.function.LowestValue;
import com.bn.ninjatrader.simulation.operation.function.PropertyValue;
import com.bn.ninjatrader.simulation.report.SimulationReport;
import com.bn.ninjatrader.simulation.statement.*;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static com.bn.ninjatrader.logical.expression.condition.Conditions.*;
import static com.bn.ninjatrader.simulation.operation.Variables.*;
import static com.bn.ninjatrader.simulation.order.type.OrderTypes.atPrice;
import static com.bn.ninjatrader.simulation.order.type.OrderTypes.marketClose;

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

        //TODO FIX BARS FROM NOW NOT WORKING!!
        //TODO Sell at price
        //TODO show % profit loss

        // Buy Condition -- EMA fan continuation
        .addStatement(ConditionalStatement.builder()
            .condition(Conditions.create()
                .add(gt(PRICE_CLOSE, EMA.withPeriod(18)))
                .add(gt(EMA.withPeriod(18), EMA.withPeriod(50)))
                .add(gt(EMA.withPeriod(50), EMA.withPeriod(100)))
                .add(gt(EMA.withPeriod(100), EMA.withPeriod(200)))
                .add(lt(HistoryValue.of(EMA.withPeriod(18)).inNumOfBarsAgo(2), HistoryValue.of(EMA.withPeriod(50)).inNumOfBarsAgo(2)))
            )
            .then(BuyOrderStatement.builder()
                .orderType(atPrice(Operations.create(PRICE_HIGH).mult(1)))
                .barsFromNow(1)
                .build())
            .build())

        // Buy Condition -- EMA bounce
//        .addStatement(ConditionalStatement.builder()
//            .condition(Conditions.create()
//                .add(gt(PRICE_CLOSE, EMA.withPeriod(18)))
//                .add(gt(EMA.withPeriod(18), EMA.withPeriod(50)))
//                .add(gt(EMA.withPeriod(50), EMA.withPeriod(100)))
//                .add(gt(EMA.withPeriod(100), EMA.withPeriod(200)))
//                .add(Conditions.or(
//                    lt(HistoryValue.of(PRICE_LOW).inNumOfBarsAgo(1), EMA.withPeriod(50)),
//                    lt(HistoryValue.of(PRICE_LOW).inNumOfBarsAgo(2), EMA.withPeriod(50)),
//                    lt(HistoryValue.of(PRICE_LOW).inNumOfBarsAgo(3), EMA.withPeriod(50))
//                ))
//            )
//            .then(BuyOrderStatement.builder().orderType(marketClose()).barsFromNow(1).build())
//            .build())

        // Sell Condition
        .addStatement(ConditionalStatement.builder()
            .condition(Conditions.create()
                .add(lt(PRICE_LOW, PropertyValue.of("LAST_PULLBACK"))))
//                .add(lt(PRICE_CLOSE, EMA.withPeriod(18)))
//                .add(lt(EMA.withPeriod(18), EMA.withPeriod(50))))
            .then(MultiStatement.builder()
                .add(SetPropertyStatement.builder().add("LAST_PULLBACK", 0).build())
                .add(SellOrderStatement.builder().orderType(marketClose()).barsFromNow(0).build())
                .build())
            .build())

        // Initialize
        .addStatement(ConditionalStatement.builder()
            .condition(eq(BAR_INDEX, 1))
            .then(SetPropertyStatement.builder().add("LAST_PULLBACK", 0).build())
            .build()
        )

        // Pullback Condition
        .addStatement(ConditionalStatement.builder()
            .condition(Conditions.create()
                .add(eq(HistoryValue.of(PRICE_LOW).inNumOfBarsAgo(4), LowestValue.of(PRICE_LOW).inNumOfBarsAgo(10)))
                .add(lt(PropertyValue.of("LAST_PULLBACK"), HistoryValue.of(PRICE_LOW).inNumOfBarsAgo(4)))
            )
            .then(MultiStatement.builder()
                .add(SetPropertyStatement.builder()
                    .add("LAST_PULLBACK", HistoryValue.of(PRICE_LOW).inNumOfBarsAgo(4))
                    .build())
//                .add(BuyOrderStatement.builder().orderType(MarketTime.CLOSE).barsFromNow(0).build())
                .build())
            .build()
        )

        .build();

    simulator.play(params);
  }
}

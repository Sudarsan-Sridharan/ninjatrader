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
import com.bn.ninjatrader.simulation.operation.function.*;
import com.bn.ninjatrader.simulation.order.OrderConfig;
import com.bn.ninjatrader.simulation.order.type.AtPrice;
import com.bn.ninjatrader.simulation.order.type.OrderTypes;
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

    final String LAST_BROKEN_TOP = "LAST_BROKEN_TOP";

    final int PULLBACK_MIN_BAR = 2;
    final int PULLBACK_NUM_OF_BARS = 5;
    final double PULLBACK_SENSITIVITY = 0.01;
    final double MAX_BUY_RISK = 0.05;

    final SimulationParams params = SimulationParams.builder()
        .symbol("PRMX")
        .from(LocalDate.now().minusYears(1))
        .to(LocalDate.now())
        .startingCash(20000)

        // Initialize
        .addStatement(ConditionalStatement.withName("Initialize")
            .condition(eq(BAR_INDEX, 1))
            .then(SetPropertyStatement.builder()
                .add("PULLBACK", 0)
                .add("TOP", 0)
                .add("TRAILING_STOP", 0)
                .add(LAST_BROKEN_TOP, 0)
                .build())
        )

        // Pullback Condition - if higher than trailing stop, move trailing stop up.
        .addStatement(ConditionalStatement.withName("Pullback")
            .condition(Conditions.and(
                eq(HistoryValue.of(PRICE_LOW).inNumOfBarsAgo(PULLBACK_MIN_BAR), LowestValue.of(PRICE_LOW).fromBarsAgo(PULLBACK_NUM_OF_BARS)),
                Conditions.or(
                    gte(PcntChangeValue.of(PRICE_LOW, HistoryValue.of(PRICE_LOW).inNumOfBarsAgo(PULLBACK_MIN_BAR)), PULLBACK_SENSITIVITY),
                    gte(PcntChangeValue.of(HistoryValue.of(PRICE_LOW).inNumOfBarsAgo(4), HistoryValue.of(PRICE_LOW).inNumOfBarsAgo(PULLBACK_MIN_BAR)), PULLBACK_SENSITIVITY)
                )
            ))
            .then(MultiStatement.of(
                SetPropertyStatement.builder()
                    .add("PULLBACK", HistoryValue.of(PRICE_LOW).inNumOfBarsAgo(PULLBACK_MIN_BAR))
                    .build(),
                ConditionalStatement.withName("Move Trailing Stop Higher")
                    .condition(gt(PropertyValue.of("PULLBACK"), PropertyValue.of("TRAILING_STOP")))
                    .then(SetPropertyStatement.builder().add("TRAILING_STOP", PropertyValue.of("PULLBACK")).build())
//                MarkStatement.withColor("blue").marker(MarkStatement.Marker.ARROWUP).numOfBarsAgo(PULLBACK_MIN_BAR)
            ))
        )

        // Buy Condition -- EMA bounce (price bounce from EMA 50)
        .addStatement(ConditionalStatement.withName("Bounce from EMA 50")
            .condition(Conditions.and(
                lte(PcntChangeValue.of(Operations.startWith(PRICE_HIGH).plus(PipValue.of(2)), LowestValue.of(PRICE_LOW).fromBarsAgo(6)), MAX_BUY_RISK),
                gt(PRICE_CLOSE, EMA.withPeriod(18)) // Price above EMA 18
            ))
            .then(MultiStatement.of(
                BuyOrderStatement.builder()
                    .orderType(AtPrice.of(Operations.startWith(PRICE_HIGH).plus(PipValue.of(2))))
                    .orderConfig(OrderConfig.withBarsFromNow(1).expireAfterNumOfBars(1))
                    .build()
                )
            )
        )

        // Price Below Trailing Stop -- Sell
        .addStatement(ConditionalStatement.withName("Sell")
            .condition(Conditions.or(
                lt(PRICE_LOW, Operations.startWith(PropertyValue.of("TRAILING_STOP")).minus(PipValue.of(2)))
            ))
            .then(ConditionalStatement.withName("Check if gap down")
                .condition(lt(PRICE_HIGH, Operations.startWith(PropertyValue.of("TRAILING_STOP")).minus(PipValue.of(2))))
                .then(
                    SellOrderStatement.builder() // Protection from gap down.
                        .orderType(OrderTypes.marketClose())
                        .build()
                )
                .otherwise(MultiStatement.of(
                    SellOrderStatement.builder() // Sell at price 2 pips below trailing stop
                        .orderType(AtPrice.of(Operations.startWith(PropertyValue.of("TRAILING_STOP")).minus(PipValue.of(2))))
                        .build()
                ))
            )
        )

        // If price above EMA 18 by over 16%
        .addStatement(ConditionalStatement.withName("Take Profit")
            .condition(Conditions.and(
                gte(Operations.startWith(PRICE_HIGH).minus(EMA.withPeriod(18)).div(EMA.withPeriod(18)), 0.16)
            ))
            .then(MultiStatement.of(
                SellOrderStatement.builder()
                    .orderType(AtPrice.of(Operations.startWith(EMA.withPeriod(18)).mult(1.16)))
                    .build()
                )
            )
        )

        // TRAILING STOP Upon Buy
        .onBuyFulfilledStatement(MultiStatement.of(
            SetPropertyStatement.builder()
                .add("TRAILING_STOP", LowestValue.of(PRICE_LOW).fromBarsAgo(6))
                .build()
            )
        )

        .build();

    simulator.play(params);
  }
}

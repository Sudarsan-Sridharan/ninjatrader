package com.bn.ninjatrader.testplay;

import com.bn.ninjatrader.calculator.guice.NtCalculatorModule;
import com.bn.ninjatrader.model.guice.NtModelModule;
import com.bn.ninjatrader.testplay.simulation.Simulation;
import com.bn.ninjatrader.testplay.simulation.SimulationFactory;
import com.bn.ninjatrader.testplay.simulation.SimulationParams;
import com.bn.ninjatrader.testplay.simulation.guice.NtSimulationModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static com.bn.ninjatrader.testplay.condition.Conditions.*;
import static com.bn.ninjatrader.testplay.operation.function.Functions.highestInLastNBars;
import static com.bn.ninjatrader.testplay.operation.function.Functions.history;
import static com.bn.ninjatrader.testplay.simulation.data.DataType.*;
import static com.bn.ninjatrader.testplay.simulation.order.MarketTime.CLOSE;
import static com.bn.ninjatrader.testplay.simulation.order.OrderParameters.buy;
import static com.bn.ninjatrader.testplay.simulation.order.OrderParameters.sell;

/**
 * Created by Brad on 8/3/16.
 */
@Singleton
public class HistoricalTestPlay implements TestPlay {
  private static final Logger log = LoggerFactory.getLogger(HistoricalTestPlay.class);

  @Inject
  private SimulationFactory simulationFactory;

  @Override
  public void test(SimulationParams params) {
    Simulation simulation = simulationFactory.create(params);
    simulation.play();
  }

  public static void main(String args[]) {
    Injector injector = Guice.createInjector(
        new NtModelModule(),
        new NtCalculatorModule(),
        new NtSimulationModule()
    );
    HistoricalTestPlay historicalTestPlay = injector.getInstance(HistoricalTestPlay.class);

    SimulationParams params = new SimulationParams();
    params.setSymbol("MEG");
    params.setFromDate(LocalDate.now().minusYears(10));
    params.setToDate(LocalDate.now());
    params.setStartingCash(100000);

    params.setBuyCondition(create()
        .add(gt(PRICE_CLOSE, SMA_21))
        .add(gt(SMA_21, SMA_50))
        .add(gt(PRICE_CLOSE, highestInLastNBars(PRICE_CLOSE, 4)))
        .add(gt(TENKAN, KIJUN))
        .add(gt(PRICE_CLOSE, history(PRICE_CLOSE, 100)))
        .add(lte(RSI_14, 80))
    );

    params.setBuyOrderParams(buy()
        .at(CLOSE)
        .barsFromNow(0)
        .build());

    params.setSellCondition(create()
        .add(lt(PRICE_CLOSE, SMA_50))
//        .add(lt(Operations.create(PRICE_CLOSE).mult(1.05), SMA_50))
    );

    params.setSellOrderParams(sell()
        .at(CLOSE)
        .barsFromNow(0)
        .build());

    historicalTestPlay.test(params);
  }


}

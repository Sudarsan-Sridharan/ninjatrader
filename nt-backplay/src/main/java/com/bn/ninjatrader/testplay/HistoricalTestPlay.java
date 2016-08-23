package com.bn.ninjatrader.testplay;

import com.bn.ninjatrader.calculator.guice.NtCalculatorModule;
import com.bn.ninjatrader.model.guice.NtModelModule;
import com.bn.ninjatrader.testplay.simulation.Simulation;
import com.bn.ninjatrader.testplay.simulation.SimulationFactory;
import com.bn.ninjatrader.testplay.simulation.SimulationParameters;
import com.bn.ninjatrader.testplay.simulation.guice.NtSimulationModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static com.bn.ninjatrader.common.data.DataType.*;
import static com.bn.ninjatrader.testplay.condition.Conditions.*;

/**
 * Created by Brad on 8/3/16.
 */
@Singleton
public class HistoricalTestPlay implements TestPlay {
  private static final Logger log = LoggerFactory.getLogger(HistoricalTestPlay.class);

  @Inject
  private SimulationFactory simulationFactory;

  @Override
  public void test(SimulationParameters params) {
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

    SimulationParameters params = new SimulationParameters();
    params.setSymbol("MEG");
    params.setFromDate(LocalDate.now().minusYears(10));
    params.setToDate(LocalDate.now());
    params.setStartingCash(100000);

    params.setBuyCondition(create()
        .add(gt(PRICE_CLOSE, SMA_10))
        .add(gt(PRICE_CLOSE, TENKAN))
        .add(gt(TENKAN, KIJUN))
    );

    params.setSellCondition(create()
        .add(lt(PRICE_CLOSE, KIJUN)));

    historicalTestPlay.test(params);
  }
}

package com.bn.ninjatrader.process.guice;

import com.bn.ninjatrader.process.annotation.CalcAllProcess;
import com.bn.ninjatrader.process.calc.*;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * Created by Brad on 4/30/16.
 */
public class NtProcessModule extends AbstractModule {

  @Override
  protected void configure() {
  }

  @Provides
  @CalcAllProcess
  private CalcProcess provideCalcAllProcess(
      CalcPriceChangeProcess calcPriceChangeProcess,
      CalcMeanProcess calcMeanProcess,
      CalcSMAProcess calcSimpleAverageProcess,
      CalcWeeklyPriceProcess calcWeeklyPriceProcess,
      CalcWeeklyPriceChangeProcess calcWeeklyPriceChangeProcess,
      CalcWeeklyMeanProcess calcWeeklyMeanProcess) {

    return new SequentialCalcProcess(
        calcPriceChangeProcess,
        calcMeanProcess,
        calcSimpleAverageProcess,
        calcWeeklyPriceProcess,
        calcWeeklyPriceChangeProcess,
        calcWeeklyMeanProcess
    );
  }
}

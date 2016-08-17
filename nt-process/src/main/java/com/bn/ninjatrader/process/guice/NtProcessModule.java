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
  private CalcProcess provideDailyCalcAllProcess(
      CalcMeanProcess calcMeanProcess,
      CalcSimpleAverageProcess calcSimpleAverageProcess,
      CalcWeeklyPriceProcess calcWeeklyPriceProcess,
      CalcWeeklyMeanProcess calcWeeklyMeanProcess) {

    return new SequentialCalcProcess(
        calcMeanProcess,
        calcSimpleAverageProcess,
        calcWeeklyPriceProcess,
        calcWeeklyMeanProcess
    );
  }
}

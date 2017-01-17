package com.bn.ninjatrader.process.guice;

import com.bn.ninjatrader.process.annotation.AllCalcProcess;
import com.bn.ninjatrader.process.calc.*;
import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import java.util.Collection;

/**
 * Created by Brad on 4/30/16.
 */
public class NtProcessModule extends AbstractModule {

  @Override
  protected void configure() {

  }

  @Provides
  @AllCalcProcess
  private Collection<CalcProcess> provideCalcAllProcess(
      CalcWeeklyPriceProcess calcWeeklyPriceProcess,
      CalcPriceChangeProcess calcPriceChangeProcess,
      CalcMeanProcess calcMeanProcess,
      CalcSMAProcess calcSMAProcess,
      CalcEMAProcess calcEMAProcess,
      CalcRSIProcess calcRSIProcess
  ) {
    return Lists.newArrayList(
        calcWeeklyPriceProcess,
        calcPriceChangeProcess,
        calcMeanProcess,
        calcSMAProcess,
        calcEMAProcess,
        calcRSIProcess
    );
  }
}

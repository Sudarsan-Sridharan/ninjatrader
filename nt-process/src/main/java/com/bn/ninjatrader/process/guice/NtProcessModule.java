package com.bn.ninjatrader.process.guice;

import com.bn.ninjatrader.process.annotation.AllCalcProcess;
import com.bn.ninjatrader.process.calc.*;
import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import java.util.List;

/**
 * Created by Brad on 4/30/16.
 */
public class NtProcessModule extends AbstractModule {

  @Override
  protected void configure() {

  }

  @Provides
  @AllCalcProcess
  private List<CalcProcess> provideCalcAllProcess(
      final CalcWeeklyPriceProcess calcWeeklyPriceProcess,
      final CalcPriceChangeProcess calcPriceChangeProcess
//      final CalcMeanProcess calcMeanProcess,
//      final CalcSMAProcess calcSMAProcess,
//      final CalcEMAProcess calcEMAProcess,
//      final CalcRSIProcess calcRSIProcess
  ) {
    return Lists.newArrayList(
        calcWeeklyPriceProcess,
        calcPriceChangeProcess
//        calcMeanProcess,
//        calcSMAProcess,
//        calcEMAProcess,
//        calcRSIProcess
    );
  }
}

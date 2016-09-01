package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.calculator.MeanCalculator;
import com.bn.ninjatrader.model.dao.WeeklyMeanDao;
import com.bn.ninjatrader.model.dao.WeeklyPriceDao;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Brad on 6/8/16.
 */
public class CalcWeeklyMeanProcess extends AbstractCalcForPeriodProcess {

  private static final Logger log = LoggerFactory.getLogger(CalcWeeklyMeanProcess.class);
  private static final int[] DEFAULT_PERIODS = {9, 26, 52};

  @Inject
  public CalcWeeklyMeanProcess(MeanCalculator calculator,
                               WeeklyPriceDao weeklyPriceDao,
                               WeeklyMeanDao weeklyMeanDao) {
    super(calculator, weeklyPriceDao, weeklyMeanDao);
  }

  @Override
  protected int[] getDefaultPeriods() {
    return DEFAULT_PERIODS;
  }
}

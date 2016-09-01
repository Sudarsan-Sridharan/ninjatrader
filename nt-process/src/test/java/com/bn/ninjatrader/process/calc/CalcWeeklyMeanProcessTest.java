package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.calculator.CalculatorForPeriod;
import com.bn.ninjatrader.calculator.MeanCalculator;
import com.bn.ninjatrader.model.dao.ValueDao;
import com.bn.ninjatrader.model.dao.WeeklyMeanDao;
import com.bn.ninjatrader.model.dao.WeeklyPriceDao;
import mockit.Injectable;
import mockit.Tested;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Brad on 6/11/16.
 */
public class CalcWeeklyMeanProcessTest extends AbstractCalcForPeriodProcessTest {

  private static final Logger log = LoggerFactory.getLogger(CalcWeeklyMeanProcessTest.class);

  @Injectable
  private MeanCalculator calculator;

  @Injectable
  private WeeklyMeanDao weeklyMeanDao;

  @Injectable
  private WeeklyPriceDao weeklyPriceDao;

  @Tested
  private CalcWeeklyMeanProcess process;

  @Override
  public ValueDao provideDao() {
    return weeklyMeanDao;
  }

  @Override
  public CalcProcess provideTestedProcess() {
    return process;
  }

  @Override
  public CalculatorForPeriod provideCalculator() {
    return calculator;
  }
}

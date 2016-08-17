package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.calculator.MeanCalculator;
import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.model.dao.WeeklyMeanDao;
import com.bn.ninjatrader.model.dao.period.SaveRequest;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Brad on 6/8/16.
 */
public class CalcWeeklyMeanProcess extends AbstractCalcForPeriodProcess {

  private static final Logger log = LoggerFactory.getLogger(CalcWeeklyMeanProcess.class);

  public static final int[] PERIODS = {9, 26, 52};

  @Inject
  private WeeklyMeanDao weeklyMeanDao;

  @Inject
  public CalcWeeklyMeanProcess(MeanCalculator calculator) {
    super(calculator);
  }

  @Override
  protected int[] getDefaultPeriods() {
    return PERIODS;
  }

  @Override
  protected void save(String symbol, int period, List<Value> values) {
    weeklyMeanDao.save(SaveRequest.forSymbol(symbol).period(period).values(values));
  }
}

package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.model.dao.WeeklyPriceDao;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Brad on 6/8/16.
 */
public class CalcWeeklyPriceChangeProcess extends CalcPriceChangeProcess {
  private static final Logger log = LoggerFactory.getLogger(CalcWeeklyPriceChangeProcess.class);

  @Inject
  public CalcWeeklyPriceChangeProcess(WeeklyPriceDao weeklyPriceDao) {
    super(weeklyPriceDao);
  }
}

package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.calculator.MeanCalculator;
import com.bn.ninjatrader.model.dao.MeanDao;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Brad on 6/8/16.
 */
public class CalcMeanProcess extends AbstractCalcForPeriodProcess {

  private static final Logger log = LoggerFactory.getLogger(CalcMeanProcess.class);

  public static final int[] PERIODS = {9, 26, 52};

  @Inject
  public CalcMeanProcess(MeanCalculator calculator,
                         PriceDao priceDao,
                         MeanDao meanDao) {
    super(calculator, priceDao, meanDao);
  }

  @Override
  protected int[] getDefaultPeriods() {
    return PERIODS;
  }
}

package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.calculator.SMACalculator;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.SMADao;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Brad on 6/8/16.
 */
public class CalcSMAProcess extends AbstractCalcForPeriodProcess {

  private static final Logger log = LoggerFactory.getLogger(CalcSMAProcess.class);
  private static final int[] DEFAULT_PERIODS = {10, 15, 20, 21, 30, 50, 100, 200};

  @Inject
  public CalcSMAProcess(SMACalculator calculator,
                        PriceDao priceDao,
                        SMADao smaDao) {
    super(calculator, priceDao, smaDao);
  }

  @Override
  protected int[] getDefaultPeriods() {
    return DEFAULT_PERIODS;
  }
}

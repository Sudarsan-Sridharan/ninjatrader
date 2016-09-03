package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.calculator.RSICalculator;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.RSIDao;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Brad on 6/8/16.
 */
public class CalcRSIProcess extends AbstractCalcValuesProcess {

  private static final Logger log = LoggerFactory.getLogger(CalcRSIProcess.class);
  private static final int[] DEFAULT_PERIODS = {10, 14, 20};

  @Inject
  public CalcRSIProcess(RSICalculator calculator,
                        PriceDao priceDao,
                        RSIDao rsiDao) {
    super(calculator, priceDao, rsiDao);
  }

  @Override
  protected int[] getDefaultPeriods() {
    return DEFAULT_PERIODS;
  }
}

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
public class CalcEMAProcess extends AbstractCalcValuesProcess {

  private static final Logger LOG = LoggerFactory.getLogger(CalcEMAProcess.class);
  private static final String PROCESS_NAME = "sma";
  private static final int[] DEFAULT_PERIODS = {10, 15, 20, 21, 30, 50, 100, 200};

  @Inject
  public CalcEMAProcess(SMACalculator calculator,
                        PriceDao priceDao,
                        SMADao smaDao) {
    super(calculator, priceDao, smaDao);
  }

  @Override
  protected int[] getDefaultPeriods() {
    return DEFAULT_PERIODS;
  }

  @Override
  public String getProcessName() {
    return PROCESS_NAME;
  }
}

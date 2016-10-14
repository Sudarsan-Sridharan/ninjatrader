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
public class CalcAllProcess extends AbstractCalcValuesProcess {

  private static final Logger LOG = LoggerFactory.getLogger(CalcAllProcess.class);
  private static final String PROCESS_NAME = "mean";

  public static final int[] PERIODS = {9, 26, 52};

  @Inject
  public CalcAllProcess(MeanCalculator calculator,
                        PriceDao priceDao,
                        MeanDao meanDao) {
    super(calculator, priceDao, meanDao);
  }

  @Override
  protected int[] getDefaultPeriods() {
    return PERIODS;
  }

  @Override
  public String getProcessName() {
    return PROCESS_NAME;
  }
}

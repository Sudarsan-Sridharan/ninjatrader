package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.calculator.SMACalculator;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.SMADao;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * Created by Brad on 6/8/16.
 */
@Singleton
public class CalcSMAProcess extends AbstractCalcValuesProcess {
  private static final Logger LOG = LoggerFactory.getLogger(CalcSMAProcess.class);
  private static final String PROCESS_NAME = "sma";
  private static final List<Integer> DEFAULT_PERIODS =
      Collections.unmodifiableList(Lists.newArrayList(10, 15, 20, 21, 30, 50, 100, 200));

  @Inject
  public CalcSMAProcess(final SMACalculator calculator,
                        final PriceDao priceDao,
                        SMADao smaDao) {
    super(calculator, priceDao, smaDao);
  }

  @Override
  protected List<Integer> getDefaultPeriods() {
    return DEFAULT_PERIODS;
  }

  @Override
  public String getProcessName() {
    return PROCESS_NAME;
  }
}

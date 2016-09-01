package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.calculator.CalculatorForPeriod;
import com.bn.ninjatrader.calculator.MeanCalculator;
import com.bn.ninjatrader.model.dao.MeanDao;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.ValueDao;
import mockit.Injectable;
import mockit.Tested;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Brad on 6/11/16.
 */
public class CalcMeanProcessTest extends AbstractCalcForPeriodProcessTest {

  private static final Logger log = LoggerFactory.getLogger(CalcMeanProcessTest.class);

  @Injectable
  private MeanCalculator calculator;

  @Injectable
  private MeanDao dao;

  @Injectable
  private PriceDao priceDao;

  @Tested
  private CalcMeanProcess process;

  @Override
  public ValueDao provideDao() {
    return dao;
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

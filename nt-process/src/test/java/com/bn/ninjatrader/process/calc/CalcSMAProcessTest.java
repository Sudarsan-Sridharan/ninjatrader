package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.calculator.ValueCalculator;
import com.bn.ninjatrader.calculator.SMACalculator;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.SMADao;
import com.bn.ninjatrader.model.dao.ValueDao;
import mockit.Injectable;
import mockit.Tested;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Brad on 6/11/16.
 */
public class CalcSMAProcessTest extends AbstractCalcValuesProcessTest {

  private static final Logger log = LoggerFactory.getLogger(CalcSMAProcessTest.class);

  @Injectable
  private SMACalculator calculator;

  @Injectable
  private SMADao smaDao;

  @Injectable
  private PriceDao priceDao;

  @Tested
  private CalcSMAProcess process;

  @Override
  public ValueDao provideDao() {
    return smaDao;
  }

  @Override
  public CalcProcess provideTestedProcess() {
    return process;
  }

  @Override
  public ValueCalculator provideCalculator() {
    return calculator;
  }
}

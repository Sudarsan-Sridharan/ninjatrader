package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.calculator.SimpleAverageCalculator;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.SimpleAverageDao;
import mockit.Injectable;
import mockit.Tested;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * Created by Brad on 6/11/16.
 */
public class CalcSimpleAverageProcessTest extends AbstractCalcForPeriodProcessTest {

  private static final Logger log = LoggerFactory.getLogger(CalcSimpleAverageProcessTest.class);

  @Injectable
  private SimpleAverageCalculator calculator;

  @Injectable
  private SimpleAverageDao simpleAverageDao;

  @Injectable
  private PriceDao priceDao;

  @Tested
  private CalcSimpleAverageProcess process;

  @Test
  public void testProcessWithNoData() {
    testProcessWithNoData(process, simpleAverageDao);
  }

  @Test
  public void testProcessWithData() {
    testProcessWithData(calculator, process, simpleAverageDao);
  }
}

package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.calculator.MeanCalculator;
import com.bn.ninjatrader.model.dao.MeanDao;
import com.bn.ninjatrader.model.dao.PriceDao;
import mockit.Injectable;
import mockit.Tested;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * Created by Brad on 6/11/16.
 */
public class CalcMeanProcessTest extends AbstractCalcForPeriodProcessTest {

  private static final Logger log = LoggerFactory.getLogger(CalcMeanProcessTest.class);

  @Injectable
  private MeanCalculator meanCalculator;

  @Injectable
  private MeanDao meanDao;

  @Injectable
  private PriceDao priceDao;

  @Tested
  private CalcMeanProcess process;

  @Test
  public void testProcessWithNoData() {
    testProcessWithNoData(process, meanDao);
  }

  @Test
  public void testProcessWithData() {
    testProcessWithData(meanCalculator, process, meanDao);
  }
}

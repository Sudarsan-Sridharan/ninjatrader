package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.calculator.PriceChangeCalculator;
import com.bn.ninjatrader.model.dao.WeeklyPriceDao;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.bn.ninjatrader.process.request.CalcRequest.forSymbol;

/**
 * Created by Brad on 8/30/16.
 */
public class CalcWeeklyPriceChangeProcessTest extends AbstractCalcPriceChangeProcessTest {

  @Tested
  private CalcWeeklyPriceChangeProcess process;

  @Injectable
  private WeeklyPriceDao weeklyPriceDao;

  @Injectable
  private PriceChangeCalculator calculator;

  @BeforeMethod
  public void setup() {
    setup(weeklyPriceDao, calculator);
  }

  @Test
  public void testProcess() {
    process.processMissingBars(forSymbol("MEG"));
    assertSaveCalled();
  }

  void assertSaveCalled() {
    new Verifications() {{
      weeklyPriceDao.save("MEG", processedPriceList);
    }};
  }
}

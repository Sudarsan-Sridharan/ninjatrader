package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.calculator.PriceChangeCalculator;
import com.bn.ninjatrader.model.dao.PriceDao;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.bn.ninjatrader.process.request.CalcRequest.forSymbol;

/**
 * Created by Brad on 8/30/16.
 */
public class CalcPriceChangeProcessTest extends AbstractCalcPriceChangeProcessTest {

  @Tested
  private CalcPriceChangeProcess process;

  @Injectable
  private PriceDao priceDao;

  @Injectable
  private PriceChangeCalculator calculator;

  @BeforeMethod
  public void setup() {
    setup(priceDao, calculator);
  }

  @Test
  public void testProcess() {
    process.processMissingBars(forSymbol("MEG"));
    assertSaveCalled();
  }

  void assertSaveCalled() {
    new Verifications() {{
      priceDao.save("MEG", processedPriceList);
    }};
  }
}

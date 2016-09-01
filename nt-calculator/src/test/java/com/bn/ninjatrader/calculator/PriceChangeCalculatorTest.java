package com.bn.ninjatrader.calculator;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.common.data.Price;
import mockit.Tested;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created by Brad on 7/11/16.
 */
public class PriceChangeCalculatorTest {
  private static final Logger log = LoggerFactory.getLogger(PriceChangeCalculatorTest.class);

  @Tested
  private PriceChangeCalculator calculator;

  private final Price price1 = new Price(1.1, 2.1, 3.1, 4.1, 1000);
  private final Price price2 = new Price(2.1, 2.2, 2.3, 2.3384, 1000);
  private final Price price3 = new Price(3.1, 3.2, 3.3, 3.4981, 1000);

  @Test
  public void testCalc() {
    List<Price> priceList = Lists.newArrayList(price1, price2, price3);

    List<Price> result = calculator.calc(priceList);

    assertNotNull(result);
    assertEquals(result.size(), 3);
    assertChangeEquals(result.get(0), 0.0);
    assertChangeEquals(result.get(1), -1.7616);
    assertChangeEquals(result.get(2), 1.1597);
  }

  private void assertChangeEquals(Price price, double expectedChange) {
    assertEquals(price.getChange(), expectedChange, "Price Change");
  }
}


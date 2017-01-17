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

  private final Price price1 = Price.builder().open(1.1).high(2.1).low(3.1).close(4.1).volume(1000).build();
  private final Price price2 = Price.builder().open(2.1).high(2.2).low(2.3).close(2.3384).volume(1000).build();
  private final Price price3 = Price.builder().open(3.1).high(3.2).low(3.3).close(3.4981).volume(1000).build();

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


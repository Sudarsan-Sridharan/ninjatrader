package com.bn.ninjatrader.calculator;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.TestUtil;
import mockit.Tested;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created by Brad on 5/27/16.
 */
public class WeeklyPriceCalculatorTest {

  @Tested
  private WeeklyPriceCalculator calculator;

  @Test
  public void testCalcWithOneWeekPrices() {
    LocalDate date = LocalDate.of(2016, 6, 6);
    Price monPrice = new Price(1.0, 1.1, 1.2, 1.3, 10000, date);
    Price tuePrice = new Price(2.0, 2.1, 2.2, 2.3, 20000, date.plusDays(1));
    Price wedPrice = new Price(3.0, 3.1, 3.2, 3.3, 30000, date.plusDays(2));
    Price thrPrice = new Price(4.0, 4.1, 4.2, 4.3, 40000, date.plusDays(3));
    Price friPrice = new Price(5.0, 5.1, 5.2, 5.3, 50000, date.plusDays(4));

    List<Price> prices = Lists.newArrayList(monPrice, tuePrice, wedPrice, thrPrice, friPrice);

    List<Price> result = calculator.calc(prices);

    assertNotNull(result);
    assertEquals(result.size(), 1);

    Price weekPrice = result.get(0);
    Price expectedWeekPrice = new Price(1.0, 5.1, 1.2, 5.3, 150000, date);
    TestUtil.assertPriceEquals(weekPrice, expectedWeekPrice);
  }

  @Test
  public void testCalcWithMultipleWeekPrices() {

    // Week 1
    LocalDate date1 = LocalDate.of(2016, 6, 6);
    Price monPrice1 = new Price(1.0, 1.1, 1.2, 1.3, 10000, date1);
    Price tuePrice1 = new Price(2.0, 2.1, 2.2, 2.3, 20000, date1.plusDays(1));

    // Week 2
    LocalDate date2 = LocalDate.of(2016, 6, 13);
    Price wedPrice2 = new Price(3.0, 3.1, 3.2, 3.3, 30000, date2.plusDays(2));
    Price thrPrice2 = new Price(4.0, 4.1, 4.2, 4.3, 40000, date2.plusDays(3));

    // Week 3
    LocalDate date3 = LocalDate.of(2016, 6, 20);
    Price monPrice3 = new Price(5.0, 5.1, 5.2, 5.3, 50000, date3);
    Price friPrice3 = new Price(6.0, 6.1, 6.2, 6.3, 50000, date3.plusDays(4));

    List<Price> prices = Lists.newArrayList(monPrice1, tuePrice1, wedPrice2, thrPrice2, monPrice3, friPrice3);

    List<Price> result = calculator.calc(prices);
    
    assertNotNull(result);
    assertEquals(result.size(), 3);

    // Verify Week 1
    Price weekPrice1 = result.get(0);
    Price expectedWeekPrice1 = new Price(1.0, 2.1, 1.2, 2.3, 30000, date1);
    TestUtil.assertPriceEquals(weekPrice1, expectedWeekPrice1);

    // Verify Week 2
    Price weekPrice2 = result.get(1);
    Price expectedWeekPrice2 = new Price(3.0, 4.1, 3.2, 4.3, 70000, date2);
    TestUtil.assertPriceEquals(weekPrice2, expectedWeekPrice2);

    // Verify Week 3
    Price weekPrice3 = result.get(2);
    Price expectedWeekPrice3 = new Price(5.0, 6.1, 5.2, 6.3, 100000, date3);
    TestUtil.assertPriceEquals(weekPrice3, expectedWeekPrice3);
  }

  @Test
  public void testCalcWithPricesInSameWeekButDifferentYears() {
    LocalDate date1 = LocalDate.of(2015, 1, 5);
    LocalDate date2 = LocalDate.of(2016, 1, 11);

    Price price1 = new Price(1.0, 2.0, 3.0, 4.0, 10000, date1);
    Price price2 = new Price(1.0, 2.0, 3.0, 4.0, 10000, date2);

    List<Price> result = calculator.calc(Lists.newArrayList(price1, price2));

    assertEquals(result.size(), 2);
    TestUtil.assertPriceEquals(result.get(0), price1);
    TestUtil.assertPriceEquals(result.get(1), price2);
  }
}

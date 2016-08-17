package com.bn.ninjatrader.calculator;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.util.TestUtil;
import mockit.Tested;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by Brad on 5/27/16.
 */
public class MovingAverageCalculatorTest {

  @Tested
  private SimpleAverageCalculator calculator;

  private LocalDate now = LocalDate.of(2016, 2, 1);
  private LocalDate tomorrow = LocalDate.of(2016, 2, 2);

  @Test
  public void testCalcWithSimplePeriod() {
    LocalDate date = LocalDate.of(2016, 1, 1);
    List<Price> priceList = Lists.newArrayList();

    Price price = TestUtil.randomPrice();
    price.setDate(date);
    price.setClose(10d);
    priceList.add(price);

    List<Value> values = calculator.calc(priceList, 1);
    assertEquals(values.size(), 1);
    assertEquals(values.get(0).getDate(), date);
    assertEquals(values.get(0).getValue(), 10d);
  }

  @Test
  public void testCalcWithBigPeriod() {
    LocalDate date = LocalDate.of(2016, 1, 1);
    List<Price> priceList = Lists.newArrayList();

    // Add dummy data
    for (int i = 0; i < 26; i++) {
      date = date.plusDays(1);
      Price price = TestUtil.randomPrice();
      price.setDate(date);
      price.setClose(2d);
      priceList.add(price);
    }

    List<Value> values = calculator.calc(priceList, 26);
    assertEquals(values.size(), 1);
    assertEquals(values.get(0).getDate(), date);
    assertEquals(values.get(0).getValue(), 2d);
  }

  @Test
  public void testCalcWithMultiplePeriods() {
    LocalDate date1 = LocalDate.of(2016, 1, 1);
    LocalDate date2 = LocalDate.of(2016, 1, 2);
    List<Price> priceList = Lists.newArrayList();

    // Add price 1. Mean should be (5 + 10) / 2
    Price price1 = TestUtil.randomPrice();
    price1.setDate(date1);
    price1.setClose(9d);
    priceList.add(price1);

    // Add price 2. Mean should be (5 + 12) / 2
    Price price2 = TestUtil.randomPrice();
    price2.setDate(date2);
    price2.setClose(11.5d);
    priceList.add(price2);

    // Calculate for multiple periods
    Map<Integer, List<Value>> valueMap = calculator.calc(priceList, 1, 2);

    // Verify result. Should have periods for 1 and 2
    assertEquals(valueMap.size(), 2);
    assertTrue(valueMap.containsKey(1));
    assertTrue(valueMap.containsKey(2));

    // Verify values for period 1
    List<Value> valuesForPeriod1 = valueMap.get(1);
    assertEquals(valuesForPeriod1.size(), 2);
    assertEquals(valuesForPeriod1.get(0).getDate(), date1);
    assertEquals(valuesForPeriod1.get(0).getValue(), 9d);
    assertEquals(valuesForPeriod1.get(1).getDate(), date2);
    assertEquals(valuesForPeriod1.get(1).getValue(), 11.5d);

    // Verify values for period 2
    List<Value> valuesForPeriod2 = valueMap.get(2);
    assertEquals(valuesForPeriod2.size(), 1);
    assertEquals(valuesForPeriod2.get(0).getDate(), date2);
    assertEquals(valuesForPeriod2.get(0).getValue(), 10.25d);
  }

  @Test
  public void testCalcWithHighPrecision() {
    // Test 1
    Price price = new Price(now, 1.0, 3, 2, 2.92847583, 10000);
    List<Value> result = calculator.calc(Lists.newArrayList(price), 1);
    assertEquals(result.size(), 1);
    assertEquals(result.get(0).getValue(), 2.92847583);

    // Test 2
    price = new Price(now, 1.0, 9.5, 9.5, 1.0000000001, 10000);
    result = calculator.calc(Lists.newArrayList(price), 1);
    assertEquals(result.get(0).getValue(), 1.0000000001);

    // Test 3
    List<Price> priceList = Lists.newArrayList();
    priceList.add(new Price(now, 1.0, 9, 9, 1.0000000001, 10000));
    priceList.add(new Price(tomorrow, 1.0, 9, 9, 1.000000002, 10000));
    result = calculator.calc(priceList, 2);
    assertEquals(result.get(0).getValue(), 1.00000000105);

    // Test 3
    priceList.clear();
    priceList.add(new Price(now, 1.0, 3, 2, 1.057839201, 10000));
    priceList.add(new Price(tomorrow, 1.0, 3, 2, 1.057839202, 10000));
    priceList.add(new Price(tomorrow.plusDays(1), 1.0, 3, 2, 1.057839203, 10000));
    result = calculator.calc(priceList, 3);
    assertEquals(result.get(0).getValue(), 1.057839202);
  }
}

package com.bn.ninjatrader.calculator;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.calculator.parameter.CalcParams;
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
public class MeanCalculatorTest {

  @Tested
  private MeanCalculator calculator;

  @Test
  public void testMeanOfPeriod() {
    int highest = 10;
    int lowest = 5;

    LocalDate date = LocalDate.of(2016, 1, 1);
    List<Price> priceList = Lists.newArrayList();

    Price price = TestUtil.randomPriceWithFloorCeil(lowest, highest);
    price.setDate(date);
    price.setLow(lowest);
    price.setHigh(highest);
    priceList.add(price);

    List<Value> values = calculator.calc(priceList, 1);
    assertEquals(values.size(), 1);
    assertEquals(values.get(0).getDate(), date);
    assertEquals(values.get(0).getValue(), 7.5d);
  }

  @Test
  public void testMeanOfBigPeriod() {
    int highest = 10;
    int lowest = 5;

    LocalDate date = LocalDate.of(2016, 1, 1);
    List<Price> priceList = Lists.newArrayList();

    // Add dummy data
    for (int i = 0; i < 24; i++) {
      Price price = TestUtil.randomPriceWithFloorCeil(lowest, highest);
      price.setDate(date);
      priceList.add(price);
      date = date.plusDays(1);
    }

    // Add lowest price
    Price price = TestUtil.randomPriceWithFloorCeil(lowest, highest);
    price.setDate(date);
    price.setLow(lowest);
    priceList.add(price);
    date = date.plusDays(1);

    // Add highest price
    price = TestUtil.randomPriceWithFloorCeil(lowest, highest);
    price.setDate(date);
    price.setHigh(highest);
    priceList.add(price);

    List<Value> values = calculator.calc(priceList, 26);
    assertEquals(values.size(), 1);
    assertEquals(values.get(0).getDate(), date);
    assertEquals(values.get(0).getValue(), 7.5d);
  }

  @Test
  public void testCalcOfMultiplePeriods() {
    int highest = 10;
    int lowest = 5;

    LocalDate date1 = LocalDate.of(2016, 1, 1);
    LocalDate date2 = LocalDate.of(2016, 1, 2);
    List<Price> priceList = Lists.newArrayList();

    // Add price 1. Mean should be (5 + 10) / 2
    Price price1 = TestUtil.randomPriceWithFloorCeil(lowest, highest);
    price1.setDate(date1);
    price1.setLow(lowest);
    price1.setHigh(highest);
    priceList.add(price1);

    // Add price 2. Mean should be (5 + 12) / 2
    highest = 12;
    Price price2 = TestUtil.randomPriceWithFloorCeil(lowest, highest);
    price2.setDate(date2);
    price2.setLow(lowest);
    price2.setHigh(highest);
    priceList.add(price2);

    // Calculate for multiple periods
    Map<Integer, List<Value>> valueMap = calculator.calc(CalcParams.withPrice(priceList).periods(1, 2));

    // Verify result. Should have periods for 1 and 2
    assertEquals(valueMap.size(), 2);
    assertTrue(valueMap.containsKey(1));
    assertTrue(valueMap.containsKey(2));

    // Verify values for period 1
    List<Value> valuesForPeriod1 = valueMap.get(1);
    assertEquals(valuesForPeriod1.size(), 2);
    assertEquals(valuesForPeriod1.get(0).getDate(), date1);
    assertEquals(valuesForPeriod1.get(0).getValue(), 7.5d);
    assertEquals(valuesForPeriod1.get(1).getDate(), date2);
    assertEquals(valuesForPeriod1.get(1).getValue(), 8.5d);

    // Verify values for period 2
    List<Value> valuesForPeriod2 = valueMap.get(2);
    assertEquals(valuesForPeriod2.size(), 1);
    assertEquals(valuesForPeriod2.get(0).getDate(), date2);
    assertEquals(valuesForPeriod2.get(0).getValue(), 8.5d);
  }

  @Test
  public void testMeanPrecision() {
    // Test 1
    Price price = new Price(LocalDate.now(), 1.0, 10.0052, 0.00101, 2.0, 10000);
    List<Value> result = calculator.calc(Lists.newArrayList(price), 1);
    assertEquals(result.size(), 1);
    assertEquals(result.get(0).getValue(), 5.003105);

    // Test 2
    price = new Price(LocalDate.now(), 1.0, 9.5, 9.5, 1.0, 10000);
    result = calculator.calc(Lists.newArrayList(price), 1);
    assertEquals(result.get(0).getValue(), 9.5);

    // Test 3
    price = new Price(LocalDate.now(), 1.0, 9.5, 9.4, 1.0, 10000);
    result = calculator.calc(Lists.newArrayList(price), 1);
    assertEquals(result.get(0).getValue(), 9.45);

    // Test 3
    price = new Price(LocalDate.now(), 1.0, 0.000051, 0.000053, 1.0, 10000);
    result = calculator.calc(Lists.newArrayList(price), 1);
    assertEquals(result.get(0).getValue(), 0.000052);
  }
}

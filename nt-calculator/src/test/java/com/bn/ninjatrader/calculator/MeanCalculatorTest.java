package com.bn.ninjatrader.calculator;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.calculator.parameter.CalcParams;
import com.bn.ninjatrader.model.deprecated.Value;
import com.bn.ninjatrader.common.model.Price;
import com.bn.ninjatrader.model.util.TestUtil;
import mockit.Tested;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.bn.ninjatrader.calculator.parameter.CalcParams.withPrices;
import static org.assertj.core.api.Assertions.assertThat;
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
    final int highest = 10;
    final int lowest = 5;
    final LocalDate date = LocalDate.of(2016, 1, 1);
    final List<Price> priceList = Lists.newArrayList();

    priceList.add(TestUtil.randomPriceBuilderWithFloorCeil(lowest, highest)
        .date(date).low(lowest).high(highest).build());

    final List<Value> values = calculator.calcForPeriod(withPrices(priceList), 1);
    assertEquals(values.size(), 1);
    assertEquals(values.get(0).getDate(), date);
    assertEquals(values.get(0).getValue(), 7.5d);
  }

  @Test
  public void testMeanOfBigPeriod() {
    final int highest = 10;
    final int lowest = 5;

    LocalDate date = LocalDate.of(2016, 1, 1);
    final List<Price> priceList = Lists.newArrayList();

    // Add dummy data
    for (int i = 0; i < 24; i++) {
      priceList.add(TestUtil.randomPriceBuilderWithFloorCeil(lowest, highest).date(date).build());
      date = date.plusDays(1);
    }

    // Add lowest price
    priceList.add(TestUtil.randomPriceBuilderWithFloorCeil(lowest, highest).date(date).low(lowest).build());
    date = date.plusDays(1);

    // Add highest price
    priceList.add(TestUtil.randomPriceBuilderWithFloorCeil(lowest, highest).date(date).high(highest).build());

    final List<Value> values = calculator.calcForPeriod(withPrices(priceList), 26);
    assertEquals(values.size(), 1);
    assertEquals(values.get(0).getDate(), date);
    assertEquals(values.get(0).getValue(), 7.5d);
  }

  @Test
  public void testCalcOfMultiplePeriods() {
    int highest = 10;
    final int lowest = 5;
    final LocalDate date1 = LocalDate.of(2016, 1, 1);
    final LocalDate date2 = LocalDate.of(2016, 1, 2);
    final List<Price> priceList = Lists.newArrayList();

    // Add price 1. Mean should be (5 + 10) / 2
    priceList.add(TestUtil.randomPriceBuilderWithFloorCeil(lowest, highest)
        .date(date1)
        .low(lowest)
        .high(highest)
        .build());

    // Add price 2. Mean should be (5 + 12) / 2
    highest = 12;
    priceList.add(TestUtil.randomPriceBuilderWithFloorCeil(lowest, highest)
        .date(date2)
        .low(lowest)
        .high(highest)
        .build());

    // Calculate for multiple periods
    final Map<Integer, List<Value>> valueMap = calculator.calc(CalcParams.withPrices(priceList).periods(1, 2));

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
    List<Value> result = calculator.calcForPeriod(withPrices(Price.builder()
        .high(10.0052).low(0.00101).build()), 1);
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getValue()).isEqualTo(5.003105);

    // Test 2
    result = calculator.calcForPeriod(withPrices(Price.builder()
        .high(9.5).low(9.5).build()), 1);
    assertThat(result.get(0).getValue()).isEqualTo(9.5);

    // Test 3
    result = calculator.calcForPeriod(withPrices(Price.builder()
        .high(9.5).low(9.4).build()), 1);
    assertThat(result.get(0).getValue()).isEqualTo(9.45);

    // Test 3
    result = calculator.calcForPeriod(withPrices(Price.builder()
        .high(0.000051).low(0.000053).build()), 1);
    assertThat(result.get(0).getValue()).isEqualTo(0.000052);
  }
}

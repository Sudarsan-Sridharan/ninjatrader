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

import static com.bn.ninjatrader.calculator.parameter.CalcParams.withPrices;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by Brad on 5/27/16.
 */
public class SMACalculatorTest {

  @Tested
  private SMACalculator calculator;

  private LocalDate now = LocalDate.of(2016, 2, 1);
  private LocalDate tomorrow = LocalDate.of(2016, 2, 2);

  @Test
  public void testCalcWithSimplePeriod() {
    final LocalDate date = LocalDate.of(2016, 1, 1);
    final List<Price> priceList = Lists.newArrayList();

    priceList.add(TestUtil.randomPriceBuilder().date(date).close(10).build());

    final List<Value> values = calculator.calcForPeriod(withPrices(priceList), 1);
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
      priceList.add(TestUtil.randomPriceBuilder().date(date).close(2d).build());
    }

    List<Value> values = calculator.calcForPeriod(withPrices(priceList), 26);
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
    priceList.add(TestUtil.randomPriceBuilder().date(date1).close(9d).build());

    // Add price 2. Mean should be (5 + 12) / 2
    priceList.add(TestUtil.randomPriceBuilder().date(date2).close(11.5).build());

    // Calculate for multiple periods
    Map<Integer, List<Value>> valueMap = calculator.calc(withPrices(priceList).periods(1, 2));

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
    List<Value> result = calculator.calcForPeriod(withPrices(Price.builder().close(2.92847583).build()), 1);
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getValue()).isEqualTo(2.928476);

    // Test 2
    result = calculator.calcForPeriod(withPrices(Price.builder().close(1.0000000001).build()), 1);
    assertThat(result.get(0).getValue()).isEqualTo(1.0);

    // Test 3
    result = calculator.calcForPeriod(withPrices(
        Price.builder().close(1.00001).build(),
        Price.builder().close(1.00002).build()), 2);
    assertThat(result.get(0).getValue()).isEqualTo(1.000015);

    // Test 3
    result = calculator.calcForPeriod(withPrices(
        Price.builder().close(1.057839201).build(),
        Price.builder().close(1.057839202).build(),
        Price.builder().close(1.057839203).build()
    ), 3);
    assertThat(result.get(0).getValue()).isEqualTo(1.057839);
  }
}

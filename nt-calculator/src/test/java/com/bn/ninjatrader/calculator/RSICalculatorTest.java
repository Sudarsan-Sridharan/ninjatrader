package com.bn.ninjatrader.calculator;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.model.deprecated.Value;
import com.bn.ninjatrader.model.entity.Price;
import mockit.Tested;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.bn.ninjatrader.calculator.parameter.CalcParams.withPrices;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by Brad on 5/27/16.
 */
public class RSICalculatorTest {

  private static final Logger log = LoggerFactory.getLogger(RSICalculatorTest.class);

  @Tested
  private RSICalculator calculator;

  private final LocalDate now = LocalDate.of(2016, 1, 1);
  private LocalDate dateCounter;

  @BeforeMethod
  public void setup() {
    dateCounter = now;
  }

  @Test
  public void testWithZeroGain() {
    List<Price> priceList = createPricesWithChanges(-1);
    List<Value> values = calculator.calcForPeriod(withPrices(priceList), 1);
    assertEquals(values.size(), 1);
    assertEquals(values.get(0).getValue(), 0.0);

    priceList = createPricesWithChanges(-1, -2, -3, -4, -5, -6, -7, -8, -9, -10);
    values = calculator.calcForPeriod(withPrices(priceList), 5);

    assertEquals(values.size(), 6);
    assertEquals(values.get(0).getValue(), 0.0);
    assertEquals(values.get(5).getValue(), 0.0);
  }

  @Test
  public void testWithZeroLoss() {
    List<Price> priceList = createPricesWithChanges(1);
    List<Value> values = calculator.calcForPeriod(withPrices(priceList), 1);
    assertEquals(values.size(), 1);
    assertEquals(values.get(0).getValue(), 100.0);

    priceList = createPricesWithChanges(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    values = calculator.calcForPeriod(withPrices(priceList), 5);

    assertEquals(values.size(), 6);
    assertEquals(values.get(0).getValue(), 100.0);
    assertEquals(values.get(5).getValue(), 100.0);
  }

  @Test
  public void testCalcWithSimplePeriod() {
    List<Price> priceList = createPricesWithChanges(1, -1);
    List<Value> values = calculator.calcForPeriod(withPrices(priceList), 2);
    assertEquals(values.get(0).getValue(), 50.0);

    priceList = createPricesWithChanges(2, -1);
    values = calculator.calcForPeriod(withPrices(priceList), 2);
    assertEquals(values.get(0).getValue(), 66.67);
  }

  @Test
  public void testCalcWithBigPeriod() {
    List<Price> priceList = createPricesWithChanges(1.11, 0.24, -0.27, -0.15, -0.05, 0.80, 0.86, -0.15,
        1.71, -0.02, -1.03, -0.63, 0.04, 0.47);
    List<Value> values = calculator.calcForPeriod(withPrices(priceList), 14);
    assertEquals(values.size(), 1);
    assertEquals(values.get(0).getValue(), 69.46);

    priceList.add(createPriceWithChange(-0.87));
    values = calculator.calcForPeriod(withPrices(priceList), 14);
    assertEquals(values.size(), 2);
    assertEquals(values.get(1).getValue(), 61.77);
  }

  @Test
  public void testCalcWithMultiplePeriods() {
    List<Price> priceList = createPricesWithChanges(1.11, 0.24, -0.27, -0.15, -0.05, 0.80, 0.86, -0.15,
        1.71, -0.02, -1.03, -0.63, 0.04, 0.47);

    Map<Integer, List<Value>> map = calculator.calc(withPrices(priceList).periods(1, 2, 14));

    assertEquals(map.size(), 3);
    assertTrue(map.keySet().contains(1));
    assertTrue(map.keySet().contains(2));
    assertTrue(map.keySet().contains(14));

    assertEquals(map.get(1).size(), 14);
    assertEquals(map.get(2).size(), 13);
    assertEquals(map.get(14).size(), 1);
  }

  private List<Price> createPricesWithChanges(final double ... changes) {
    List<Price> priceList = Lists.newArrayList(changes.length);
    for (double change : changes) {
      priceList.add(createPriceWithChange(change));
    }
    return priceList;
  }

  private Price createPriceWithChange(double change) {
    Price price = Price.builder().date(dateCounter).change(change).build();
    dateCounter = dateCounter.plusDays(1);
    return price;
  }
}

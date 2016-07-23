package com.bn.ninjatrader.calculator;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.calculator.parameter.IchimokuParameters;
import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.util.TestUtil;
import com.google.inject.Guice;
import com.google.inject.Injector;
import mockit.Tested;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created by Brad on 7/11/16.
 */
public class IchimokuCalculatorTest {
  private static final Logger log = LoggerFactory.getLogger(IchimokuCalculatorTest.class);
  private static final int MAX_NUM_OF_VALUES = 10;

  @Tested
  private IchimokuCalculator ichimokuCalculator;

  private List<Price> priceList = Lists.newArrayList();
  private List<Value> tenkanList = Lists.newArrayList();
  private List<Value> kijunList = Lists.newArrayList();
  private List<Value> senkouBList = Lists.newArrayList();

  private IchimokuParameters parameters;
  private LocalDate startingDate = LocalDate.of(2016, 1, 1);

  @BeforeClass
  public void setupBeforeClass() {
    initData(MAX_NUM_OF_VALUES);

    parameters = IchimokuParameters.builder()
        .priceList(priceList)
        .tenkanList(tenkanList)
        .kijunList(kijunList)
        .senkouBList(senkouBList)
        .chickouShiftBackPeriods(26)
        .senkouShiftForwardPeriods(1)
        .build();
  }

  @BeforeMethod
  public void setup() {
    Injector injector = Guice.createInjector();
    ichimokuCalculator = injector.getInstance(IchimokuCalculator.class);
  }

  @Test
  public void testIchimokuWithNoShift() {
    int numOfRecords = 10;
    initData(numOfRecords);

    IchimokuParameters parameters = IchimokuParameters.builder()
        .priceList(priceList)
        .tenkanList(tenkanList)
        .kijunList(kijunList)
        .senkouBList(senkouBList)
        .build();

    List<Ichimoku> ichimokuList = ichimokuCalculator.calc(parameters);

    assertNotNull(ichimokuList);
    assertEquals(ichimokuList.size(), MAX_NUM_OF_VALUES);

    int i = 1;
    LocalDate date = startingDate;
    for (Ichimoku ichimoku : ichimokuList) {
      Ichimoku expectedIchimoku = new Ichimoku(date, i, i, i*2, i*3.0/2, i*3);
      TestUtil.assertIchimokuEquals(ichimoku, expectedIchimoku);
      i++;
      date = getNextWeekday(date);
    }
  }

  @Test
  public void testCalcIchimokuWithChikouShift() {
    int numOfRecords = 2;
    initData(numOfRecords);

    IchimokuParameters parameters = IchimokuParameters.builder()
        .priceList(priceList)
        .tenkanList(tenkanList)
        .kijunList(kijunList)
        .senkouBList(senkouBList)
        .chickouShiftBackPeriods(1)
        .senkouShiftForwardPeriods(0)
        .build();

    List<Ichimoku> ichimokuList = ichimokuCalculator.calc(parameters);

    assertNotNull(ichimokuList);
    assertEquals(ichimokuList.size(), 2);

    Ichimoku expectedIchimoku1 = new Ichimoku(startingDate, 2d, 1d, 2d, 1.5, 3d);
    TestUtil.assertIchimokuEquals(ichimokuList.get(0), expectedIchimoku1);

    Ichimoku expectedIchimoku2 = new Ichimoku(getNextWeekday(startingDate), 0d, 2d, 4d, 3d, 6d);
    TestUtil.assertIchimokuEquals(ichimokuList.get(1), expectedIchimoku2);
  }

  @Test
  public void testCalcIchimokuWithSenkouShift() {
    int numOfRecords = 2;
    initData(numOfRecords);

    IchimokuParameters parameters = IchimokuParameters.builder()
        .priceList(priceList)
        .tenkanList(tenkanList)
        .kijunList(kijunList)
        .senkouBList(senkouBList)
        .chickouShiftBackPeriods(0)
        .senkouShiftForwardPeriods(1)
        .build();

    List<Ichimoku> ichimokuList = ichimokuCalculator.calc(parameters);

    assertNotNull(ichimokuList);
    assertEquals(ichimokuList.size(), 3);

    Ichimoku expectedIchimoku1 = new Ichimoku(startingDate, 1d, 1d, 2d, 0d, 0d);
    TestUtil.assertIchimokuEquals(ichimokuList.get(0), expectedIchimoku1);

    Ichimoku expectedIchimoku2 = new Ichimoku(getNextWeekday(startingDate), 2d, 2d, 4d, 1.5d, 3d);
    TestUtil.assertIchimokuEquals(ichimokuList.get(1), expectedIchimoku2);

    Ichimoku expectedIchimoku3 = new Ichimoku(null, 0d, 0d, 0d, 3d, 6d);
    TestUtil.assertIchimokuEquals(ichimokuList.get(2), expectedIchimoku3);
  }

  @Test
  public void testCalcIchimokuWithChikouAndSenkouShift() {
    int numOfRecords = 2;
    initData(numOfRecords);

    IchimokuParameters parameters = IchimokuParameters.builder()
        .priceList(priceList)
        .tenkanList(tenkanList)
        .kijunList(kijunList)
        .senkouBList(senkouBList)
        .chickouShiftBackPeriods(1)
        .senkouShiftForwardPeriods(1)
        .build();

    List<Ichimoku> ichimokuList = ichimokuCalculator.calc(parameters);

    assertNotNull(ichimokuList);
    assertEquals(ichimokuList.size(), 3);

    Ichimoku expectedIchimoku1 = new Ichimoku(startingDate, 2d, 1d, 2d, 0d, 0d);
    TestUtil.assertIchimokuEquals(ichimokuList.get(0), expectedIchimoku1);

    Ichimoku expectedIchimoku2 = new Ichimoku(getNextWeekday(startingDate), 0d, 2d, 4d, 1.5d, 3d);
    TestUtil.assertIchimokuEquals(ichimokuList.get(1), expectedIchimoku2);

    Ichimoku expectedIchimoku3 = new Ichimoku(null, 0d, 0d, 0d, 3d, 6d);
    TestUtil.assertIchimokuEquals(ichimokuList.get(2), expectedIchimoku3);
  }

  private void initData(int numOfData) {
    clearData();

    LocalDate date = startingDate;

    for (int i = 1; i <= numOfData; i++) {
      priceList.add(new Price(i, i, i, i, 1000, date));
      tenkanList.add(new Value(date, i));
      kijunList.add(new Value(date, i * 2));
      senkouBList.add(new Value(date, i * 3));
      date = getNextWeekday(date);
    }
  }

  private void clearData() {
    priceList.clear();
    tenkanList.clear();
    kijunList.clear();
    senkouBList.clear();
  }

  private LocalDate getNextWeekday(LocalDate date) {
    if (date.getDayOfWeek() == DayOfWeek.FRIDAY) {
      return date.plusDays(3);
    } else {
      return date.plusDays(1);
    }
  }
}


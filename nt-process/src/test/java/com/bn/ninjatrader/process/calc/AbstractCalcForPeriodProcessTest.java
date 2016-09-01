package com.bn.ninjatrader.process.calc;

import com.beust.jcommander.internal.Maps;
import com.bn.ninjatrader.calculator.CalculatorForPeriod;
import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.model.dao.ValueDao;
import com.bn.ninjatrader.model.dao.period.SaveRequest;
import com.bn.ninjatrader.process.request.CalcRequest;
import com.google.common.collect.Lists;
import mockit.Expectations;
import mockit.Verifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.bn.ninjatrader.process.request.CalcRequest.forSymbol;
import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 8/16/16.
 */
public abstract class AbstractCalcForPeriodProcessTest {

  private static final Logger log = LoggerFactory.getLogger(AbstractCalcForPeriodProcessTest.class);

  static final String SYMBOL = "MEG";
  static final int PERIOD_WITH_NO_VALUES = 5;
  static final int PERIOD_1 = 20;
  static final int PERIOD_2 = 50;
  static final int PERIOD_3 = 100;
  static final int[] PERIODS = new int[] { PERIOD_WITH_NO_VALUES, PERIOD_1, PERIOD_2, PERIOD_3 };

  final LocalDate date1 = LocalDate.of(2014, 1, 1);
  final LocalDate date2 = LocalDate.of(2015, 1, 1);
  final LocalDate date3 = LocalDate.of(2016, 1, 1);

  final Value value1 = new Value(date1, 100d);
  final Value value2 = new Value(date2, 200d);
  final Value value3 = new Value(date3, 300d);
  final Value value4 = new Value(date3, 400d);

  final Map<Integer, List<Value>> calcResult = Maps.newHashMap();

  private ValueDao dao;
  private CalculatorForPeriod calculator;

  @BeforeClass
  public void setupData() {
    calcResult.put(PERIOD_WITH_NO_VALUES, Lists.newArrayList());
    calcResult.put(PERIOD_1, Lists.newArrayList(value1, value2, value3, value4));
    calcResult.put(PERIOD_2, Lists.newArrayList(value2, value3, value4));
    calcResult.put(PERIOD_3, Lists.newArrayList(value3, value4));
  }

  @BeforeMethod
  public void setupTest() {
    provideTestedProcess();
    calculator = provideCalculator();
    dao = provideDao();
  }

  @Test
  public void testProcessWithNoData() {
    LocalDate date = LocalDate.MIN;
    provideTestedProcess().processMissingBars(forSymbol(SYMBOL).from(date).to(date).periods(PERIODS));
    assertSaveNotCalled();
  }

  void assertSaveNotCalled() {
    new Verifications() {{
      dao.save(withInstanceOf(SaveRequest.class));
      times = 0;
    }};
  }

  @Test
  void testProcessWithData() {
    new Expectations() {{
      calculator.calc(withInstanceOf(List.class), PERIODS);
      result = calcResult;
    }};
    provideTestedProcess().processMissingBars(CalcRequest.forSymbol(SYMBOL).from(date2).to(date3).periods(PERIODS));
    assertSaveCalledForEachPeriodOnDao();
  }

  public abstract ValueDao provideDao();

  public abstract CalcProcess provideTestedProcess();

  public abstract CalculatorForPeriod provideCalculator();

  void assertSaveCalledForEachPeriodOnDao() {
    new Verifications() {{
      List<SaveRequest> saveRequestList = Lists.newArrayList();

      dao.save(withCapture(saveRequestList)); times = 3;

      Map<Integer, SaveRequest> periodToSaveRequestMap = toPeriodToSaveRequestMap(saveRequestList);
      assertEquals(periodToSaveRequestMap.size(), 3);

      SaveRequest saveRequest = periodToSaveRequestMap.get(PERIOD_1);
      assertEquals(saveRequest, SaveRequest.save(SYMBOL).period(PERIOD_1).values(value2, value3, value4));

      saveRequest = periodToSaveRequestMap.get(PERIOD_2);
      assertEquals(saveRequest, SaveRequest.save(SYMBOL).period(PERIOD_2).values(value2, value3, value4));

      saveRequest = periodToSaveRequestMap.get(PERIOD_3);
      assertEquals(saveRequest, SaveRequest.save(SYMBOL).period(PERIOD_3).values(value3, value4));
    }};
  }

  private Map<Integer, SaveRequest> toPeriodToSaveRequestMap(List<SaveRequest> saveRequestList) {
    Map<Integer, SaveRequest> periodToSaveRequestMap = Maps.newHashMap();
    for (SaveRequest saveRequest : saveRequestList) {
      periodToSaveRequestMap.put(saveRequest.getPeriod(), saveRequest);
    }
    return periodToSaveRequestMap;
  }
}

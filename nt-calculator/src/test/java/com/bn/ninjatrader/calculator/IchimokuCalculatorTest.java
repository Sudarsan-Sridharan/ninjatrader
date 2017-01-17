package com.bn.ninjatrader.calculator;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.calculator.parameter.IchimokuParameters;
import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.data.Value;
import com.google.inject.Guice;
import com.google.inject.Injector;
import mockit.Tested;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 7/11/16.
 */
public class IchimokuCalculatorTest {
  private static final Logger log = LoggerFactory.getLogger(IchimokuCalculatorTest.class);

  @Tested
  private IchimokuCalculator ichimokuCalculator;

  private List<Price> priceList = Lists.newArrayList();
  private List<Value> tenkanList = Lists.newArrayList();
  private List<Value> kijunList = Lists.newArrayList();
  private List<Value> senkouBList = Lists.newArrayList();

  private IchimokuParameters parameters;
  private LocalDate startingDate = LocalDate.of(2016, 1, 1);

  @BeforeMethod
  public void setup() {
    final Injector injector = Guice.createInjector();
    ichimokuCalculator = injector.getInstance(IchimokuCalculator.class);
  }

  @Test
  public void testIchimokuWithNoShift() {
    initNumOfTestData(10);

    final IchimokuParameters parameters = IchimokuParameters.builder()
        .priceList(priceList)
        .tenkanList(tenkanList)
        .kijunList(kijunList)
        .senkouBList(senkouBList)
        .build();

    final List<Ichimoku> ichimokuList = ichimokuCalculator.calc(parameters);

    assertThat(ichimokuList).hasSize(10);

    int i = 1;
    LocalDate date = startingDate;
    for (Ichimoku ichimoku : ichimokuList) {
      assertThat(ichimoku).isEqualTo(Ichimoku.builder()
          .date(date).chikou(i).tenkan(i).kijun(i*2).senkouA(i*3.0/2).senkouB(i*3).build());
      i++;
      date = getNextWeekday(date);
    }
  }

  @Test
  public void testCalcIchimokuWithChikouShift() {
    initNumOfTestData(2);

    final IchimokuParameters parameters = IchimokuParameters.builder()
        .priceList(priceList)
        .tenkanList(tenkanList)
        .kijunList(kijunList)
        .senkouBList(senkouBList)
        .chickouShiftBackPeriods(1) // Shift by 1 period back
        .build();

    final List<Ichimoku> ichimokuList = ichimokuCalculator.calc(parameters);

    assertThat(ichimokuList).hasSize(2).containsExactly(
        Ichimoku.builder().date(startingDate).chikou(2d).tenkan(1d).kijun(2d).senkouA(1.5).senkouB(3d).build(),
        Ichimoku.builder().date(getNextWeekday(startingDate)).tenkan(2d).kijun(4d).senkouA(3d).senkouB(6d).build()
    );
  }

  @Test
  public void testCalcIchimokuWithSenkouShift() {
    int numOfRecords = 2;
    initNumOfTestData(numOfRecords);

    final IchimokuParameters parameters = IchimokuParameters.builder()
        .priceList(priceList)
        .tenkanList(tenkanList)
        .kijunList(kijunList)
        .senkouBList(senkouBList)
        .senkouShiftForwardPeriods(1) // Shift 1 period forward
        .build();

    final List<Ichimoku> ichimokuList = ichimokuCalculator.calc(parameters);

    assertThat(ichimokuList).hasSize(3).containsExactly(
        Ichimoku.builder().date(startingDate).chikou(1d).tenkan(1d).kijun(2d).build(),
        Ichimoku.builder().date(getNextWeekday(startingDate))
            .chikou(2d).tenkan(2d).kijun(4d).senkouA(1.5d).senkouB(3d).build(),
        Ichimoku.builder().senkouA(3d).senkouB(6d).build()
    );
  }

  @Test
  public void testCalcIchimokuWithChikouAndSenkouShift() {
    final int numOfRecords = 2;
    initNumOfTestData(numOfRecords);

    final IchimokuParameters parameters = IchimokuParameters.builder()
        .priceList(priceList)
        .tenkanList(tenkanList)
        .kijunList(kijunList)
        .senkouBList(senkouBList)
        .chickouShiftBackPeriods(1)
        .senkouShiftForwardPeriods(1)
        .build();

    final List<Ichimoku> ichimokuList = ichimokuCalculator.calc(parameters);

    assertThat(ichimokuList).hasSize(3).containsExactly(
        Ichimoku.builder().date(startingDate).chikou(2d).tenkan(1d).kijun(2d).build(),
        Ichimoku.builder().date(getNextWeekday(startingDate))
            .tenkan(2d).kijun(4d).senkouA(1.5d).senkouB(3d).build(),
        Ichimoku.builder().senkouA(3d).senkouB(6d).build()
    );
  }

  private void initNumOfTestData(int numOfData) {
    clearTestData();

    LocalDate date = startingDate;

    for (int i = 1; i <= numOfData; i++) {
      priceList.add(Price.builder().date(date).open(i).high(i).low(i).close(i).volume(1000).build());
      tenkanList.add(new Value(date, i));
      kijunList.add(new Value(date, i * 2));
      senkouBList.add(new Value(date, i * 3));
      date = getNextWeekday(date);
    }
  }

  private void clearTestData() {
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


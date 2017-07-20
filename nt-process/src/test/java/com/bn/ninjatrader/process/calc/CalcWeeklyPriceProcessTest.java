package com.bn.ninjatrader.process.calc;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.calculator.WeeklyPriceCalculator;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.process.request.CalcRequest;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_SELF;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Brad on 8/16/16.
 */
public class CalcWeeklyPriceProcessTest {

  private final LocalDate fromDate = LocalDate.of(2016, 2, 5);
  private final LocalDate toDate = LocalDate.of(2016, 2, 10);

  private final LocalDate week1 = LocalDate.of(2016, 2, 1);
  private final LocalDate week2 = LocalDate.of(2016, 2, 8);
  private final LocalDate week3 = LocalDate.of(2016, 2, 15);

  private final Price weeklyPrice1 = Price.builder()
      .date(week1).open(1.1).high(1.2).low(1.3).close(1.4).volume(1000).build();
  private final Price weeklyPrice2 = Price.builder()
      .date(week2).open(2.1).high(2.2).low(2.3).close(2.4).volume(2000).build();
  private final Price weeklyPrice3 = Price.builder()
      .date(week3).open(3.1).high(3.2).low(3.3).close(3.4).volume(3000).build();

  private WeeklyPriceCalculator calculator;
  private PriceDao priceDao;
  private CalcWeeklyPriceProcess process;

  @Before
  public void setup() {
    calculator = mock(WeeklyPriceCalculator.class);
    priceDao = mock(PriceDao.class);
    process = new CalcWeeklyPriceProcess(calculator, priceDao);

    when(priceDao.savePrices(any())).thenReturn(mock(PriceDao.SavePricesOperation.class, RETURNS_SELF));
    when(priceDao.findPrices()).thenReturn(mock(PriceDao.FindPricesOperation.class, RETURNS_SELF));
    when(priceDao.findPrices().now()).thenReturn(Collections.emptyList());
    when(calculator.calc(any())).thenReturn(Lists.newArrayList(weeklyPrice1, weeklyPrice2, weeklyPrice3));
  }

  @Test
  public void testProcessBars_shouldAdjustFromDateToStartOfWeek() {
    process.process(CalcRequest.forSymbol("MEG").from(fromDate).to(toDate));

    verify(priceDao.findPrices()).withSymbol("MEG");
    verify(priceDao.findPrices()).from(LocalDate.of(2016, 2, 1));
    verify(priceDao.findPrices()).to(LocalDate.of(2016, 2, 10));
  }

  @Test
  public void testCalcWithMultipleSymbols_shouldCalcForEachGivenSymbol() {
    process.process(CalcRequest.forSymbols("MEG", "BDO").from(fromDate).to(toDate));

    verify(priceDao.findPrices()).withSymbol("MEG");
    verify(priceDao.findPrices()).withSymbol("BDO");

    verify(priceDao.savePrices(any())).withSymbol("MEG");
    verify(priceDao.savePrices(any())).withSymbol("BDO");
  }

  @Test
  public void testSaveOnlyValuesWithinRequestedDates() {
    process.process(CalcRequest.forSymbol("MEG").from(fromDate).to(toDate));

    verify(priceDao).savePrices(Lists.newArrayList(weeklyPrice2));
    verify(priceDao.savePrices(any())).withTimeFrame(TimeFrame.ONE_WEEK);
  }
}

package com.bn.ninjatrader.process.calc;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.calculator.WeeklyPriceCalculator;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.WeeklyPriceDao;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static com.bn.ninjatrader.process.request.CalcRequest.forSymbol;
import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 8/16/16.
 */
public class CalcWeeklyPriceProcessTest {

  private static final String SYMBOL = "MEG";

  @Injectable
  private WeeklyPriceCalculator calculator;

  @Injectable
  private PriceDao priceDao;

  @Injectable
  private WeeklyPriceDao weeklyPriceDao;

  @Tested
  private CalcWeeklyPriceProcess process;

  private final LocalDate week1 = LocalDate.of(2016, 2, 1);
  private final LocalDate week2 = LocalDate.of(2016, 2, 8);
  private final LocalDate week3 = LocalDate.of(2016, 2, 15);

  private final Price weeklyPrice1 = new Price(week1, 1.1, 1.2, 1.3, 1.4, 1000);
  private final Price weeklyPrice2 = new Price(week2, 2.1, 2.2, 2.3, 2.4, 2000);
  private final Price weeklyPrice3 = new Price(week3, 3.1, 3.2, 3.3, 3.4, 3000);

  @BeforeMethod
  public void setupExpectations() {
    new Expectations() {{
      priceDao.findByDateRange(SYMBOL, withInstanceOf(LocalDate.class), withInstanceOf(LocalDate.class));
      result = Collections.emptyList();

      calculator.calc(withInstanceOf(List.class));
      result = Lists.newArrayList(weeklyPrice1, weeklyPrice2, weeklyPrice3);
    }};
  }

  @Test
  public void testProcessBars() {
    LocalDate fromDate = LocalDate.of(2016, 2, 5);
    LocalDate toDate = LocalDate.of(2016, 2, 10);

    process.processMissingBars(forSymbol(SYMBOL).from(fromDate).to(toDate));

    assertDateAdjustedToStartOfWeek();
  }

  private void assertDateAdjustedToStartOfWeek() {
    new Verifications() {{
      LocalDate fromDate, toDate;

      priceDao.findByDateRange(SYMBOL, fromDate = withCapture() , toDate = withCapture());
      times = 1;

      assertEquals(fromDate, LocalDate.of(2016, 2, 1));
      assertEquals(toDate, LocalDate.of(2016, 2, 10));
    }};
  }

  @Test
  public void testSaveOnlyValuesWithinRequestedDates() {
    LocalDate fromDate = LocalDate.of(2016, 2, 5);
    LocalDate toDate = LocalDate.of(2016, 2, 10);

    process.processMissingBars(forSymbol(SYMBOL).from(fromDate).to(toDate));

    assertSaveValuesWithinRequestedDates();
  }

  private void assertSaveValuesWithinRequestedDates() {
    new Verifications() {{
      List<Price> prices;
      weeklyPriceDao.save(SYMBOL, prices = withCapture()); times = 1;
      assertEquals(prices.size(), 1);
      assertEquals(prices.get(0), weeklyPrice2);
    }};
  }
}

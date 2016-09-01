package com.bn.ninjatrader.process.calc;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.process.request.CalcRequest;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 8/30/16.
 */
public class AbstractCalcProcessTest {

  @Tested
  private AbstractCalcProcess process;

  @Injectable
  private PriceDao priceDao;

  private final LocalDate date1 = LocalDate.of(2016, 8, 1);
  private final LocalDate date2 = LocalDate.of(2016, 8, 2);
  private final LocalDate date3 = LocalDate.of(2016, 8, 3);
  private final LocalDate date4 = LocalDate.of(2016, 8, 4);
  private final LocalDate date5 = LocalDate.of(2016, 8, 5);

  private final Price price1 = new Price(date1, 1, 1, 1, 1, 1000);
  private final Price price2 = new Price(date2, 2, 2, 2, 2, 2000);
  private final Price price3 = new Price(date3, 3, 3, 3, 3, 3000);
  private final Price price4 = new Price(date4, 4, 4, 4, 4, 4000);
  private final Price price5 = new Price(date5, 5, 5, 5, 5, 5000);

  @BeforeMethod
  public void setup() {
    new Expectations() {{
      priceDao.findNBarsBeforeDate("MEG", 1, date3);
      result = Lists.newArrayList(price2);
      minTimes = 0;

      priceDao.findNBarsBeforeDate("MEG", 2, date3);
      result = Lists.newArrayList(price1, price2);
      minTimes = 0;

      priceDao.findNBarsBeforeDate("MEG", 100, date3);
      result = Lists.newArrayList(price1, price2);
      minTimes = 0;
    }};
  }

  @Test
  public void testGetFromDateToHaveEnoughBars() {
    LocalDate fromDate = process.getFromDateToHaveEnoughBars(CalcRequest
        .forSymbol("MEG")
        .from(date3)
        .to(date5), 1);
    assertEquals(fromDate, date2);

    fromDate = process.getFromDateToHaveEnoughBars(CalcRequest
        .forSymbol("MEG")
        .from(date3)
        .to(date5), 2);
    assertEquals(fromDate, date1);
  }

  @Test
  public void testWithBarsOutOfRange() {
    LocalDate fromDate = process.getFromDateToHaveEnoughBars(CalcRequest
        .forSymbol("MEG")
        .from(date3)
        .to(date5), 100);
    assertEquals(fromDate, date3);
  }
}

package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.calculator.RSICalculator;
import com.bn.ninjatrader.calculator.ValueCalculator;
import com.bn.ninjatrader.calculator.parameter.CalcParams;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.data.RSIValue;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.RSIDao;
import com.bn.ninjatrader.model.dao.ValueDao;
import com.bn.ninjatrader.model.request.FindRequest;
import com.bn.ninjatrader.process.request.CalcRequest;
import com.google.common.collect.Lists;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;

import static org.testng.Assert.*;

/**
 * Created by Brad on 6/11/16.
 */
public class CalcRSIProcessTestWithDb extends AbstractCalcValuesProcessTest {

  private static final Logger log = LoggerFactory.getLogger(CalcRSIProcessTestWithDb.class);

  private final int period1 = 10;
  private final int period2 = 20;
  private final int invalidPeriod = 45;

  private final LocalDate date1 = LocalDate.of(2014, 1, 1);
  private final LocalDate date2 = LocalDate.of(2015, 1, 1);
  private final LocalDate date3 = LocalDate.of(2016, 1, 1);

  private final Price price1 = new Price(date1, 1.1, 1.2, 1.3, 1.4, 1000);
  private final Price price2 = new Price(date2, 2.1, 2.2, 2.3, 2.4, 2000);
  private final Price price3 = new Price(date3, 3.1, 3.2, 3.3, 3.4, 3000);

  private final RSIValue rsiValue1 = new RSIValue(date1, 98.5, 0.234, 0.123);
  private final RSIValue rsiValue2 = new RSIValue(date2, 97.4, 0.334, 0.223);
  private final RSIValue rsiValue3 = new RSIValue(date3, 95.3, 0.434, 0.323);

  @Injectable
  private RSICalculator calculator;

  @Injectable
  private RSIDao rsiDao;

  @Injectable
  private PriceDao priceDao;

  @Tested
  private CalcRSIProcess process;

  @BeforeMethod
  public void setupExpectations() {
    new Expectations() {{
      priceDao.find(withInstanceOf(FindRequest.class));
      minTimes = 0;
      result = Lists.newArrayList(price1, price2, price3);

      rsiDao.find(withInstanceOf(FindRequest.class));
      minTimes = 0;
      result = Lists.newArrayList(rsiValue1);
    }};
  }

  @Test
  public void testProvideCalcParams() {
    new Expectations() {{
      rsiDao.findNBarsBeforeDate("MEG", 1, date1, period1);
      result = Lists.newArrayList(rsiValue1);

      rsiDao.findNBarsBeforeDate("MEG", 1, date1, period2);
      result = Lists.newArrayList(rsiValue1);
    }};

    CalcRequest calcRequest = CalcRequest.forSymbol("MEG").from(date1).to(date3);
    List<Price> prices = Lists.newArrayList(price1, price2, price3);

    CalcParams params = process.provideCalcParams(calcRequest, prices, period1, period2);

    assertEquals(params.getPrices().size(), 3);
    assertEquals(params.getPeriods().length, 2);
    assertTrue(params.getContinueFromValueForPeriod(period1).isPresent());
    assertTrue(params.getContinueFromValueForPeriod(period2).isPresent());
    assertFalse(params.getContinueFromValueForPeriod(invalidPeriod).isPresent());
  }

  @Override
  public ValueDao provideDao() {
    return rsiDao;
  }

  @Override
  public CalcProcess provideTestedProcess() {
    return process;
  }

  @Override
  public ValueCalculator provideCalculator() {
    return calculator;
  }
}

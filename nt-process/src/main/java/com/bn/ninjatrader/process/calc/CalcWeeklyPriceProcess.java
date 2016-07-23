package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.calculator.WeeklyPriceCalculator;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.WeeklyPriceDao;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Brad on 6/8/16.
 */
public class CalcWeeklyPriceProcess implements CalcProcess {
  private static final Logger log = LoggerFactory.getLogger(CalcWeeklyPriceProcess.class);

  @Inject
  private WeeklyPriceCalculator calculator;

  @Inject
  private PriceDao priceDao;

  @Inject
  private WeeklyPriceDao weeklyPriceDao;

  public void process(String symbol, LocalDate fromDate, LocalDate toDate) {
    List<Price> prices = priceDao.findByDateRange(symbol, fromDate, toDate);

    // Calculate weekly prices
    List<Price> weeklyPrices = calculator.calc(prices);

    weeklyPriceDao.save(symbol, weeklyPrices);
  }
}

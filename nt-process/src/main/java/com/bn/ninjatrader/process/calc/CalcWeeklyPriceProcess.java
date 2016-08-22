package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.calculator.WeeklyPriceCalculator;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.DateObjUtil;
import com.bn.ninjatrader.common.util.DateUtil;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.WeeklyPriceDao;
import com.bn.ninjatrader.process.request.CalcRequest;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

import static com.bn.ninjatrader.model.dao.period.FindRequest.forSymbol;

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

  public void processPrices(CalcRequest calcRequest) {
    String symbol = calcRequest.getSymbol();
    LocalDate fromDate = calcRequest.getFromDate();
    LocalDate toDate = calcRequest.getToDate();
    log.debug("Calculating weekly price. Sym: {}, from: {}, to: {}", symbol, fromDate, toDate);

    List<Price> prices = priceDao.find(forSymbol(symbol).from(fromDate).to(toDate));
    List<Price> weeklyPrices = calculator.calc(prices);

    weeklyPriceDao.save(symbol, weeklyPrices);
  }

  @Override
  public void processMissingBars(CalcRequest calcRequest) {
    log.debug("with {}", calcRequest);
    String symbol = calcRequest.getSymbol();
    LocalDate priceFromDate = DateUtil.toStartOfWeek(calcRequest.getFromDate());
    LocalDate priceToDate = calcRequest.getToDate();

    List<Price> prices = priceDao.find(forSymbol(symbol).from(priceFromDate).to(priceToDate));
    List<Price> weeklyPrices = calculator.calc(prices);

    DateObjUtil.trimToDateRange(weeklyPrices, calcRequest.getFromDate(), calcRequest.getToDate());
    if (!weeklyPrices.isEmpty()) {
      weeklyPriceDao.save(symbol, weeklyPrices);
    }
  }
}

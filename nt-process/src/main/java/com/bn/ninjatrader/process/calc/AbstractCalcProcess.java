package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.process.request.CalcRequest;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Brad on 7/28/16.
 */
@Singleton
public abstract class AbstractCalcProcess implements CalcProcess {

  private static final Logger log = LoggerFactory.getLogger(CalcMeanProcess.class);

  private final PriceDao priceDao;

  @Inject
  public AbstractCalcProcess(PriceDao priceDao) {
    this.priceDao = priceDao;
  }

  public LocalDate getFromDateToHaveEnoughBars(CalcRequest calcRequest, int numOfBars) {
    String symbol = calcRequest.getSymbol();
    LocalDate fromDate = calcRequest.getFromDate();

    List<Price> prices = priceDao.findNBarsBeforeDate(symbol, numOfBars, fromDate);
    if (!prices.isEmpty() && prices.size() == numOfBars) {
      return prices.get(0).getDate();
    }
    return calcRequest.getFromDate();
  }
}

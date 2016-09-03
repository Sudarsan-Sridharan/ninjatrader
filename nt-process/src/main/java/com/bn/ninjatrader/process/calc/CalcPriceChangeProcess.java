package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.calculator.PriceChangeCalculator;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.process.request.CalcRequest;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

import static com.bn.ninjatrader.model.request.FindRequest.forSymbol;

/**
 * Created by Brad on 6/8/16.
 */
public class CalcPriceChangeProcess extends AbstractCalcProcess implements CalcProcess {
  private static final Logger log = LoggerFactory.getLogger(CalcPriceChangeProcess.class);

  @Inject
  private PriceChangeCalculator calculator;

  private final PriceDao priceDao;

  @Inject
  public CalcPriceChangeProcess(PriceDao priceDao) {
    super(priceDao);
    this.priceDao = priceDao;
  }

  @Override
  public void processMissingBars(CalcRequest calcRequest) {
    String symbol = calcRequest.getSymbol();
    LocalDate fromDate = getFromDateToHaveEnoughBars(calcRequest, 1);
    LocalDate toDate = calcRequest.getToDate();

    List<Price> priceList = priceDao.find(forSymbol(symbol).from(fromDate).to(toDate));

    priceList = calculator.calc(priceList);

    priceDao.save(symbol, priceList);
  }
}

package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.calculator.PriceChangeCalculator;
import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.request.FindPriceRequest;
import com.bn.ninjatrader.model.request.SavePriceRequest;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.request.FindBeforeDateRequest;
import com.bn.ninjatrader.process.request.CalcRequest;
import com.bn.ninjatrader.process.util.CalcProcessNames;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Brad on 6/8/16.
 */
@Singleton
public class CalcPriceChangeProcess extends AbstractCalcProcess implements CalcProcess {
  private static final Logger LOG = LoggerFactory.getLogger(CalcPriceChangeProcess.class);
  private static final int REQUIRED_NUM_OF_PAST_BARS = 1;

  private final PriceChangeCalculator calculator;
  private final PriceDao priceDao;

  @Inject
  public CalcPriceChangeProcess(final PriceChangeCalculator calculator,
                                final PriceDao priceDao) {
    super(priceDao);
    this.calculator = calculator;
    this.priceDao = priceDao;
  }

  @Override
  public void process(final CalcRequest calcRequest) {
    for (final String symbol : calcRequest.getAllSymbols()) { // For each symbol
      for (final TimeFrame timeFrame : calcRequest.getTimeFrames()) { // and for each time frame

        final LocalDate fromDate = getFromDateToHaveEnoughBars(FindBeforeDateRequest.builder()
            .symbol(symbol).timeFrame(timeFrame)
            .beforeDate(calcRequest.getFromDate())
            .numOfValues(REQUIRED_NUM_OF_PAST_BARS)
            .build());
        final LocalDate toDate = calcRequest.getToDate();

        List<Price> priceList = priceDao.find(FindPriceRequest.forSymbol(symbol).timeframe(timeFrame).from(fromDate).to(toDate));
        priceList = calculator.calc(priceList);

        priceDao.save(SavePriceRequest.forSymbol(symbol).timeframe(timeFrame).addPrices(priceList));
      }
    }
  }

  @Override
  public String getProcessName() {
    return CalcProcessNames.PRICE_CHANGE;
  }

}

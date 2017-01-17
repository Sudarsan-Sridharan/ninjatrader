package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.calculator.WeeklyPriceCalculator;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.common.util.DateObjUtil;
import com.bn.ninjatrader.common.util.DateUtil;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.request.SaveRequest;
import com.bn.ninjatrader.process.request.CalcRequest;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

import static com.bn.ninjatrader.model.request.FindRequest.findSymbol;

/**
 * Created by Brad on 6/8/16.
 */
@Singleton
public class CalcWeeklyPriceProcess implements CalcProcess {
  private static final Logger LOG = LoggerFactory.getLogger(CalcWeeklyPriceProcess.class);
  private static final String PROCESS_NAME = "weekly-price";

  private final WeeklyPriceCalculator calculator;
  private final PriceDao priceDao;

  @Inject
  public CalcWeeklyPriceProcess(final WeeklyPriceCalculator calculator,
                                final PriceDao priceDao) {
    this.calculator = calculator;
    this.priceDao = priceDao;
  }

  @Override
  public void process(final CalcRequest calcRequest) {
    LOG.debug("with {}", calcRequest);
    final String symbol = calcRequest.getSymbol();
    final LocalDate priceFromDate = DateUtil.toStartOfWeek(calcRequest.getFromDate());
    final LocalDate priceToDate = calcRequest.getToDate();

    final List<Price> prices = priceDao.find(findSymbol(symbol).from(priceFromDate).to(priceToDate));
    final List<Price> weeklyPrices = calculator.calc(prices);

    DateObjUtil.trimToDateRange(weeklyPrices, calcRequest.getFromDate(), calcRequest.getToDate());
    if (!weeklyPrices.isEmpty()) {
      priceDao.save(SaveRequest.save(symbol)
          .timeFrame(TimeFrame.ONE_WEEK)
          .values(weeklyPrices));
    }
  }

  @Override
  public String getProcessName() {
    return PROCESS_NAME;
  }
}

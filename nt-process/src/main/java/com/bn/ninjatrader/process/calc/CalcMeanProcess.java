package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.calculator.MeanCalculator;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.model.dao.MeanDao;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Created by Brad on 6/8/16.
 */
public class CalcMeanProcess implements CalcProcess {

  private static final Logger log = LoggerFactory.getLogger(CalcMeanProcess.class);

  public static final int[] PERIODS = {9, 26, 52};

  @Inject
  private MeanCalculator meanCalculator;

  @Inject
  private MeanDao meanDao;

  @Inject
  private PriceDao priceDao;

  public void process(String symbol, LocalDate fromDate, LocalDate toDate) {
    List<Price> prices = priceDao.findByDateRange(symbol, fromDate, toDate);

    // Calculate mean
    Map<Integer, List<Value>> meanMap = meanCalculator.calc(prices, PERIODS);

    // Save mean for each period
    for (Map.Entry<Integer, List<Value>> entry : meanMap.entrySet()) {
      meanDao.save(symbol, entry.getKey(), entry.getValue());
    }
  }
}

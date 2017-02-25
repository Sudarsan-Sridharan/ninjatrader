package com.bn.ninjatrader.process.calc;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.request.FindBeforeDateRequest;
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

  private static final Logger LOG = LoggerFactory.getLogger(AbstractCalcProcess.class);

  private final PriceDao priceDao;

  @Inject
  public AbstractCalcProcess(final PriceDao priceDao) {
    this.priceDao = priceDao;
  }

  public LocalDate getFromDateToHaveEnoughBars(final FindBeforeDateRequest request) {
    final List<Price> prices = priceDao.findBeforeDate(request);
    if (!prices.isEmpty() && prices.size() == request.getNumOfValues()) {
      return prices.get(0).getDate();
    }
    return request.getBeforeDate();
  }
}

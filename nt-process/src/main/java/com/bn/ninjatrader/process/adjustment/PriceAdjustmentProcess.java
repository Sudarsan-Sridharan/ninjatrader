package com.bn.ninjatrader.process.adjustment;

import com.bn.ninjatrader.calculator.PriceAdjustmentCalculator;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.request.FindPriceRequest;
import com.bn.ninjatrader.model.request.SavePriceRequest;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class PriceAdjustmentProcess {
  private static final Logger LOG = LoggerFactory.getLogger(PriceAdjustmentProcess.class);

  private final PriceDao priceDao;
  private final PriceAdjustmentCalculator calculator;

  @Inject
  public PriceAdjustmentProcess(final PriceDao priceDao,
                                final PriceAdjustmentCalculator calculator) {
    this.priceDao = priceDao;
    this.calculator = calculator;
  }

  public void process(final PriceAdjustmentRequest request) {
    checkNotNull(request, "request must not be null.");
    checkNotNull(request.getOperation(), "request.adjustment must not be null.");

    final List<Price> prices = priceDao.find(FindPriceRequest.forSymbol(request.getSymbol())
        .from(request.getFromDate()).to(request.getToDate()));

    final List<Price> adjustedPrices = calculator.calc(prices, request.getOperation());

    adjustedPrices.forEach(price -> LOG.info("{}", price));

    priceDao.save(SavePriceRequest.forSymbol(request.getSymbol())
        .timeframe(TimeFrame.ONE_DAY)
        .addPrices(adjustedPrices));
  }
}

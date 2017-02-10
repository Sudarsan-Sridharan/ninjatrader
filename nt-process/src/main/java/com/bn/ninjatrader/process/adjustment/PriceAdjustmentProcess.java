package com.bn.ninjatrader.process.adjustment;

import com.bn.ninjatrader.calculator.PriceAdjustmentCalculator;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.request.FindRequest;
import com.bn.ninjatrader.model.request.SaveRequest;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class PriceAdjustmentProcess {

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

    final Set<String> symbols = request.isForAllSymbols() ? priceDao.findAllSymbols() : request.getSymbols();
    symbols.forEach(symbol -> adjustPricesForSymbol(symbol, request));
  }

  /**
   * Adjust prices for symbol.
   */
  private void adjustPricesForSymbol(final String symbol, final PriceAdjustmentRequest req) {
    final List<Price> prices = priceDao.find(FindRequest
        .findSymbol(symbol).from(req.getFromDate()).to(req.getToDate()));

    final List<Price> adjustedPrices = calculator.calc(prices, req.getOperation());

    priceDao.save(SaveRequest.save(symbol).timeFrame(TimeFrame.ONE_DAY).values(adjustedPrices));
  }
}

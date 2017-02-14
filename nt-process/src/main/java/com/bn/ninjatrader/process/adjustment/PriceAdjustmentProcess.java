package com.bn.ninjatrader.process.adjustment;

import com.bn.ninjatrader.calculator.PriceAdjustmentCalculator;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.logical.expression.operation.Operations;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.guice.NtModelModule;
import com.bn.ninjatrader.model.request.FindRequest;
import com.bn.ninjatrader.model.request.SaveRequest;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
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

    final List<Price> prices = priceDao.find(FindRequest
        .findSymbol(request.getSymbol()).from(request.getFromDate()).to(request.getToDate()));

    final List<Price> adjustedPrices = calculator.calc(prices, request.getOperation());

    adjustedPrices.forEach(price -> LOG.info("{}", price));

    priceDao.save(SaveRequest.save(request.getSymbol()).timeFrame(TimeFrame.ONE_DAY).values(adjustedPrices));
  }

  public static void main(String args[]) {
    final Injector injector = Guice.createInjector(new NtModelModule());
    final PriceAdjustmentProcess process = injector.getInstance(PriceAdjustmentProcess.class);

    process.process(PriceAdjustmentRequest.forSymbol("MWIDE")
        .from(LocalDate.now().minusYears(100))
        .to(LocalDate.of(2013, 7, 15))
        .adjustment(Operations.startWith(PriceAdjustmentRequest.PRICE).mult(0.769454545)));
  }
}

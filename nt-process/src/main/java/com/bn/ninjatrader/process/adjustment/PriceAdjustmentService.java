package com.bn.ninjatrader.process.adjustment;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.entity.PriceBuilderFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class PriceAdjustmentService {
  private static final Logger LOG = LoggerFactory.getLogger(PriceAdjustmentService.class);
  private static final GroovyShell shell = new GroovyShell();
  private static final String PRICE_VARIABLE = "$PRICE";
  private final PriceDao priceDao;
  private final PriceBuilderFactory priceBuilderFactory;

  @Inject
  public PriceAdjustmentService(final PriceDao priceDao,
                                final PriceBuilderFactory priceBuilderFactory) {
    this.priceDao = priceDao;
    this.priceBuilderFactory = priceBuilderFactory;
  }

  public PriceAdjustmentRequest.ExecutorBuilder preparePriceAdjustment() {
    return new PriceAdjustmentRequest.ExecutorBuilder(this);
  }

  public List<Price> adjustPrices(final PriceAdjustmentRequest request) {
    checkNotNull(request, "request must not be null.");

    final List<Price> prices = priceDao.findPrices()
        .withSymbol(request.getSymbol())
        .from(request.getFrom())
        .to(request.getTo())
        .now();

    if (prices.isEmpty()) {
      return Collections.emptyList();
    }

    final Script script = shell.parse(request.getScript());

    final List<Price> adjustedPrices = prices.stream()
        .map(price -> adjustPriceWithScript(price, script))
        .collect(Collectors.toList());

    priceDao.savePrices(adjustedPrices)
        .withSymbol(request.getSymbol())
        .withTimeFrame(TimeFrame.ONE_DAY)
        .now();

    return adjustedPrices;
  }

  private Price adjustPriceWithScript(final Price price, final Script script) {
    script.setProperty(PRICE_VARIABLE, price.getOpen());
    final Double open = (Double) script.run();

    script.setProperty(PRICE_VARIABLE, price.getHigh());
    final Double high = (Double) script.run();

    script.setProperty(PRICE_VARIABLE, price.getLow());
    final Double low = (Double) script.run();

    script.setProperty(PRICE_VARIABLE, price.getClose());
    final Double close = (Double) script.run();

    return priceBuilderFactory.builder().copyOf(price)
        .open(open)
        .high(high)
        .low(low)
        .close(close)
        .build();
  }
}

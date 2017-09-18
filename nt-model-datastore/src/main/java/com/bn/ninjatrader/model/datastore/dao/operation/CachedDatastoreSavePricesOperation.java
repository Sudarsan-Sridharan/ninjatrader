package com.bn.ninjatrader.model.datastore.dao.operation;

import com.bn.ninjatrader.common.model.Price;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.datastore.document.PriceDocument;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Builder for finding prices operation
 */
public final class CachedDatastoreSavePricesOperation implements PriceDao.SavePricesOperation {

  private final Map<String, List<Price>> cachedPrices;
  private final DatastoreSavePricesOperation savePricesOperation;

  public CachedDatastoreSavePricesOperation(final Map<String, List<Price>> cachedPrices,
                                            final Collection<Price> prices) {
    this.cachedPrices = cachedPrices;
    this.savePricesOperation = new DatastoreSavePricesOperation(prices);
  }

  @Override
  public CachedDatastoreSavePricesOperation withSymbol(final String symbol) {
    this.savePricesOperation.withSymbol(symbol);
    return this;
  }

  @Override
  public CachedDatastoreSavePricesOperation withTimeFrame(final TimeFrame timeFrame) {
    this.savePricesOperation.withTimeFrame(timeFrame);
    return this;
  }

  @Override
  public void now() {
    final Set<Integer> yearsAffected = savePricesOperation.getPrices().stream()
        .map((price) ->  price.getDate().getYear()).collect(Collectors.toSet());

    yearsAffected.stream().forEach((year) -> clearCache(year));

    savePricesOperation.now();
  }

  private void clearCache(final int year) {
    cachedPrices.remove(PriceDocument.id(savePricesOperation.getSymbol(), year, savePricesOperation.getTimeFrame()));
  }
}

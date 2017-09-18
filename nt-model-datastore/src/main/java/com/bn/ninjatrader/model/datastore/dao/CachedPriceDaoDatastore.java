package com.bn.ninjatrader.model.datastore.dao;

import com.bn.ninjatrader.common.model.Price;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.request.FindBeforeDateRequest;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class CachedPriceDaoDatastore implements PriceDao {

  private final PriceDao priceDao;
  private final Map<String, List<Price>> cachedPrices;

  @Inject
  public CachedPriceDaoDatastore(final PriceDao priceDao, final Map<String, List<Price>> cachedPrices) {
    this.priceDao = priceDao;
    this.cachedPrices = cachedPrices;
  }

  @Override
  public SavePricesOperation savePrices(final Collection<Price> prices) {
    return null;
  }

  @Override
  public SavePricesOperation savePrices(final Price price, final Price... more) {
    return null;
  }

  @Override
  public FindPricesOperation findPrices() {
    return null;
  }

  @Override
  public Set<String> findAllSymbols() {
    return null;
  }

  @Override
  public List<Price> findBeforeDate(FindBeforeDateRequest build) {
    return null;
  }

  @Override
  public RenameSymbolOperation renameSymbol(String symbol) {
    return null;
  }
}

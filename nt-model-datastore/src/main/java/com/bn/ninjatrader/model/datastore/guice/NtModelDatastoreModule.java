package com.bn.ninjatrader.model.datastore.guice;

import com.bn.ninjatrader.cache.client.api.CacheClient;
import com.bn.ninjatrader.common.model.Price;
import com.bn.ninjatrader.model.dao.AlgorithmDao;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.UserDao;
import com.bn.ninjatrader.model.datastore.annotation.PriceCache;
import com.bn.ninjatrader.model.datastore.dao.AlgorithmDaoDatastore;
import com.bn.ninjatrader.model.datastore.dao.PriceDaoDatastore;
import com.bn.ninjatrader.model.datastore.dao.UserDaoDatastore;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import java.util.List;
import java.util.Map;

/**
 * @author bradwee2000@gmail.com
 */
public class NtModelDatastoreModule extends AbstractModule {

  private static final String CACHED_PRICES_NAMESPACE = "prices";

  @Override
  protected void configure() {
    bind(AlgorithmDao.class).to(AlgorithmDaoDatastore.class);
    bind(PriceDao.class).to(PriceDaoDatastore.class);
    bind(UserDao.class).to(UserDaoDatastore.class);
  }

  @Provides
  @PriceCache
  public Map<String, List<Price>> provideCachedPrices(final CacheClient cacheClient) {
    return cacheClient.getMap(CACHED_PRICES_NAMESPACE);
  }
}

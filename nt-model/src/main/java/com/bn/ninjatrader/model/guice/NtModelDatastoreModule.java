package com.bn.ninjatrader.model.guice;

import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.datastore.PriceDaoDatastore;
import com.google.inject.AbstractModule;

/**
 * @author bradwee2000@gmail.com
 */
public class NtModelDatastoreModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(PriceDao.class).to(PriceDaoDatastore.class);
  }
}

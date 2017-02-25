package com.bn.ninjatrader.model.datastore.guice;

import com.bn.ninjatrader.model.entity.PriceBuilderFactory;
import com.bn.ninjatrader.common.guice.NtClockModule;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.ReportDao;
import com.bn.ninjatrader.model.datastore.dao.PriceDaoDatastore;
import com.bn.ninjatrader.model.datastore.dao.ReportDaoDatastore;
import com.bn.ninjatrader.model.datastore.factory.PriceBuilderFactoryDatastore;
import com.google.inject.AbstractModule;

/**
 * @author bradwee2000@gmail.com
 */
public class NtModelDatastoreModule extends AbstractModule {
  
  @Override
  protected void configure() {
    install(new NtClockModule());
    bind(PriceDao.class).to(PriceDaoDatastore.class);
    bind(ReportDao.class).to(ReportDaoDatastore.class);
    bind(PriceBuilderFactory.class).to(PriceBuilderFactoryDatastore.class);
  }
}

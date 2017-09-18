package com.bn.ninjatrader.model.datastore.guice;

import com.bn.ninjatrader.model.dao.AlgorithmDao;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.UserDao;
import com.bn.ninjatrader.model.datastore.dao.AlgorithmDaoDatastore;
import com.bn.ninjatrader.model.datastore.dao.PriceDaoDatastore;
import com.bn.ninjatrader.model.datastore.dao.UserDaoDatastore;
import com.google.inject.AbstractModule;

/**
 * @author bradwee2000@gmail.com
 */
public class NtModelDatastoreModule extends AbstractModule {
  
  @Override
  protected void configure() {
    bind(AlgorithmDao.class).to(AlgorithmDaoDatastore.class);
    bind(PriceDao.class).to(PriceDaoDatastore.class);
    bind(UserDao.class).to(UserDaoDatastore.class);
  }
}

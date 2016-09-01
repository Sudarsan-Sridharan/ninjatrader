package com.bn.ninjatrader.model.dao;

/**
 * Created by Brad on 5/4/16.
 */
public class RSIDaoTest extends AbstractPeriodDaoTest {

  @Override
  public ValueDao initDao() {
    return getInjector().getInstance(RSIDao.class);
  }
}

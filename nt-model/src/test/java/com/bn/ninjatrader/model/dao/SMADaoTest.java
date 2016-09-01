package com.bn.ninjatrader.model.dao;

/**
 * Created by Brad on 5/4/16.
 */
public class SMADaoTest extends AbstractPeriodDaoTest {

  @Override
  public ValueDao initDao() {
    return getInjector().getInstance(SMADao.class);
  }
}

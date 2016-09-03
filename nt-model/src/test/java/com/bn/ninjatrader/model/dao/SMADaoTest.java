package com.bn.ninjatrader.model.dao;

/**
 * Created by Brad on 5/4/16.
 */
public class SMADaoTest extends AbstractValueDaoTest {

  @Override
  public ValueDao provideTestedDao() {
    return getInjector().getInstance(SMADao.class);
  }
}

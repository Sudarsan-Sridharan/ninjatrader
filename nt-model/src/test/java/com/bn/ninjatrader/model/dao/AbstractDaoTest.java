package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.model.guice.NtModelTestModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Created by Brad on 5/4/16.
 */
public abstract class AbstractDaoTest {

  private Injector injector;

  public AbstractDaoTest() {
    injector = Guice.createInjector(
        new NtModelTestModule()
    );
  }

  public Injector getInjector() {
    return injector;
  }
}

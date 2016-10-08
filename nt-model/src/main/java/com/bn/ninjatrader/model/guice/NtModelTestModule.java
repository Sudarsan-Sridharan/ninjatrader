package com.bn.ninjatrader.model.guice;

/**
 * Created by Brad on 4/30/16.
 */
public class NtModelTestModule extends NtModelModule {

  public static final String TEST_MONGODB_NAME = "test_ninja_trader";

  @Override
  public String getMongodbName() {
    return TEST_MONGODB_NAME;
  }
}

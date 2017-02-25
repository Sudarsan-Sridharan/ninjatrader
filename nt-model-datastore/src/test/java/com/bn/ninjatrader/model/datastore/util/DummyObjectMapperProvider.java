package com.bn.ninjatrader.model.datastore.util;

import com.bn.ninjatrader.model.datastore.guice.NtModelDatastoreModule;
import com.bn.ninjatrader.model.util.ObjectMapperProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author bradwee2000@gmail.com
 */
public class DummyObjectMapperProvider {

  public static ObjectMapper om;

  public static ObjectMapper get() {
    if (om == null) {
      om = create();
    }
    return om;
  }

  private static ObjectMapper create() {
    final Injector injector = Guice.createInjector(new NtModelDatastoreModule());
    return injector.getInstance(ObjectMapperProvider.class).get();
  }
}

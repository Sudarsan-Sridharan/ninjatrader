package com.bn.ninjatrader.model.datastore.factory;

import com.bn.ninjatrader.model.entity.PriceBuilder;
import com.bn.ninjatrader.model.entity.PriceBuilderFactory;
import com.bn.ninjatrader.model.datastore.builder.PriceBuilderDatastore;
import com.google.inject.Singleton;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class PriceBuilderFactoryDatastore implements PriceBuilderFactory {

  @Override
  public PriceBuilder builder() {
    return PriceBuilderDatastore.newInstance();
  }
}

package com.bn.ninjatrader.model.mongo.factory;

import com.bn.ninjatrader.model.entity.PriceBuilder;
import com.bn.ninjatrader.model.entity.PriceBuilderFactory;
import com.bn.ninjatrader.model.mongo.builder.PriceBuilderMongo;

/**
 * @author bradwee2000@gmail.com
 */
public class PriceBuilderFactoryMongo implements PriceBuilderFactory {
  @Override
  public PriceBuilder builder() {
    return PriceBuilderMongo.newInstance();
  }
}

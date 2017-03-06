package com.bn.ninjatrader.model.entity;

import com.bn.ninjatrader.model.util.IdGenerator;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class TradeAlgorithmFactory {

  private final IdGenerator idGenerator;

  @Inject
  public TradeAlgorithmFactory(final IdGenerator idGenerator) {
    this.idGenerator = idGenerator;
  }

  public TradeAlgorithm.Builder create() {
    return TradeAlgorithm.builder().id(idGenerator.createId());
  }
}

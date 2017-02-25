package com.bn.ninjatrader.model.mongo.document;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.googlecode.objectify.annotation.Entity;

/**
 * Created by Brad on 6/3/16.
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceMongoDocument extends AbstractDocument<Price> {

  public PriceMongoDocument() {
    super();
  }

  public PriceMongoDocument(final String symbol, final int year) {
    super(symbol, year);
  }

  public PriceMongoDocument(final String symbol, final int year, final TimeFrame timeFrame) {
    super(symbol, year, timeFrame);
  }
}

package com.bn.ninjatrader.model.document;

import com.bn.ninjatrader.common.data.Price;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Brad on 6/3/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceDocument extends AbstractDocument<Price> {

  public PriceDocument() {
    super();
  }

  public PriceDocument(final String symbol, final int year) {
    super(symbol, year);
  }
}

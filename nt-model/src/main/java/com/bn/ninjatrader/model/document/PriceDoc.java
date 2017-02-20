package com.bn.ninjatrader.model.document;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.googlecode.objectify.annotation.Entity;

/**
 * Created by Brad on 6/3/16.
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceDoc extends AbstractDocument<Price> {

  public PriceDoc() {
    super();
  }

  public PriceDoc(final String symbol, final int year) {
    super(symbol, year);
  }

  public PriceDoc(final String symbol, final int year, final TimeFrame timeFrame) {
    super(symbol, year, timeFrame);
  }
}

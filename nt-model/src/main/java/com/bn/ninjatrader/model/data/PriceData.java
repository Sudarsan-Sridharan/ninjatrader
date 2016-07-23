package com.bn.ninjatrader.model.data;

import com.bn.ninjatrader.common.data.Price;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Brad on 6/3/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceData extends AbstractStockData<Price> {

  public PriceData() {
    super();
  }

  public PriceData(String symbol, int year) {
    super(symbol, year);
  }
}

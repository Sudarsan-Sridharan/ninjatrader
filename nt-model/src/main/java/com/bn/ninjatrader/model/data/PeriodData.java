package com.bn.ninjatrader.model.data;

import com.bn.ninjatrader.common.data.Value;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Brad on 6/3/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PeriodData extends AbstractStockData<Value> {

  public static final String PERIOD = "pr";

  @JsonProperty(PERIOD)
  private int period;

  public PeriodData() {
    super();
  }

  public PeriodData(String symbol, int year, int period) {
    super(symbol, year);
    this.period = period;
  }

  public int getPeriod() {
    return period;
  }

  public void setPeriod(int period) {
    this.period = period;
  }
}

package com.bn.ninjatrader.model.document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Brad on 6/3/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbstractPerPeriodDocument<T> extends AbstractDocument<T> {

  public static final String PERIOD = "pr";

  @JsonProperty(PERIOD)
  private int period;

  public AbstractPerPeriodDocument() {
    super();
  }

  public AbstractPerPeriodDocument(String symbol, int year, int period) {
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

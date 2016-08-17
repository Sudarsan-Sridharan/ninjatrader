package com.bn.ninjatrader.model.dao.period;

import com.bn.ninjatrader.common.data.Value;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by Brad on 7/27/16.
 */
public class SaveRequest {
  private String symbol;
  private int period;
  private List<Value> values;
  private AbstractPeriodDao dao;

  public SaveRequest(AbstractPeriodDao dao) {
    this.dao = dao;
  }

  public SaveRequest symbol(String symbol) {
    this.symbol = symbol;
    return this;
  }

  public SaveRequest period(int period) {
    this.period = period;
    return this;
  }

  public SaveRequest values(List<Value> values) {
    this.values = values;
    return this;
  }

  public SaveRequest values(Value ... values) {
    this.values = Lists.newArrayList(values);
    return this;
  }

  public void execute() {
    Preconditions.checkArgument(StringUtils.isNotEmpty(symbol));
    Preconditions.checkArgument(period > 0);
    Preconditions.checkArgument(values != null);
    dao.save(symbol, period, values);
  }
}

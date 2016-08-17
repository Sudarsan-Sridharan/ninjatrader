package com.bn.ninjatrader.model.dao.period;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;

/**
 * Created by Brad on 7/27/16.
 */
public class FindRequest {
  private String symbol;
  private int period;
  private LocalDate fromDate;
  private LocalDate toDate;
  private AbstractPeriodDao dao;

  public FindRequest(AbstractPeriodDao dao) {
    this.dao = dao;
  }

  public FindRequest symbol(String symbol) {
    this.symbol = symbol;
    return this;
  }

  public FindRequest period(int period) {
    this.period = period;
    return this;
  }

  public FindRequest from(LocalDate fromDate) {
    this.fromDate = fromDate;
    return this;
  }

  public FindRequest to(LocalDate toDate) {
    this.toDate = toDate;
    return this;
  }

  public void execute() {
    Preconditions.checkArgument(StringUtils.isNotEmpty(symbol));
    Preconditions.checkArgument(period > 0);
    Preconditions.checkArgument(fromDate != null);
    Preconditions.checkArgument(toDate != null);
    dao.findByDateRange(symbol, period, fromDate, toDate);
  }
}

package com.bn.ninjatrader.service.model;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.common.util.DateUtil;
import com.bn.ninjatrader.model.request.FindRequest;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author bradwee2000@gmail.com
 */
public class MultiPeriodRequest {

  @PathParam("symbol")
  private String symbol;

  @QueryParam("timeframe")
  private TimeFrame timeFrame;

  @QueryParam("period")
  private List<Integer> periods;

  @QueryParam("from")
  private LocalDate from;

  @QueryParam("to")
  private LocalDate to;

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public TimeFrame getTimeFrame() {
    return timeFrame;
  }

  public void setTimeFrame(TimeFrame timeFrame) {
    this.timeFrame = timeFrame;
  }

  public List<Integer> getPeriods() {
    return periods;
  }

  public void setPeriods(List<Integer> periods) {
    this.periods = periods;
  }

  public void setPeriods(Integer period, Integer ... morePeriods) {
    periods = Lists.asList(period, morePeriods);
  }

  public LocalDate getFrom() {
    return from;
  }

  public void setFrom(LocalDate from) {
    this.from = from;
  }

  public LocalDate getTo() {
    return to;
  }

  public void setTo(LocalDate to) {
    this.to = to;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("symbol", symbol)
        .append("timeFrame", timeFrame)
        .append("period", periods)
        .append("from", from)
        .append("to", to)
        .build();
  }
}

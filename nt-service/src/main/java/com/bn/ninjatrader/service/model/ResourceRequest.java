package com.bn.ninjatrader.service.model;

import com.bn.ninjatrader.common.type.TimeFrame;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.time.LocalDate;
import java.util.Optional;

/**
 * @author bradwee2000@gmail.com
 */
public class ResourceRequest {

  @PathParam("symbol")
  private String symbol;

  @QueryParam("timeframe")
  private Optional<TimeFrame> timeFrame;

  @QueryParam("period")
  private int period;

  @QueryParam("from")
  private Optional<LocalDate> from;

  @QueryParam("to")
  private Optional<LocalDate> to;

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public Optional<TimeFrame> getTimeFrame() {
    return timeFrame;
  }

  public void setTimeFrame(Optional<TimeFrame> timeFrame) {
    this.timeFrame = timeFrame;
  }

  public int getPeriod() {
    return period;
  }

  public void setPeriod(int period) {
    this.period = period;
  }

  public Optional<LocalDate> getFrom() {
    return from;
  }

  public void setFrom(Optional<LocalDate> from) {
    this.from = from;
  }

  public Optional<LocalDate> getTo() {
    return to;
  }

  public void setTo(Optional<LocalDate> to) {
    this.to = to;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(ToStringStyle.SHORT_PREFIX_STYLE)
        .append("symbol", symbol)
        .append("timeFrame", timeFrame)
        .append("period", period)
        .append("from", from)
        .append("to", to)
        .build();
  }
}

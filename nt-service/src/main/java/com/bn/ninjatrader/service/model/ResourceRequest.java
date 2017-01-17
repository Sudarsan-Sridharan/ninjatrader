package com.bn.ninjatrader.service.model;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.request.FindRequest;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.time.Clock;
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
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("symbol", symbol)
        .append("timeFrame", timeFrame)
        .append("from", from)
        .append("to", to)
        .build();
  }

  public FindRequest toFindRequest(Clock clock) {
    return FindRequest.findSymbol(symbol)
        .timeframe(timeFrame.orElse(TimeFrame.ONE_DAY))
        .from(from.orElse(LocalDate.now(clock).minusYears(2)))
        .to(to.orElse(LocalDate.now(clock)));
  }
}

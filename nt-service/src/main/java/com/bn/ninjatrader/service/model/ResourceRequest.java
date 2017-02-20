package com.bn.ninjatrader.service.model;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.request.FindRequest;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Optional;

/**
 * @author bradwee2000@gmail.com
 */
public class ResourceRequest {
  private static final Logger LOG = LoggerFactory.getLogger(ResourceRequest.class);

  @PathParam("symbol")
  private String symbol;

  @QueryParam("timeframe")
  private TimeFrame timeFrame;

  @QueryParam("from")
  private LocalDate from;

  @QueryParam("to")
  private LocalDate to;

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(final String symbol) {
    this.symbol = symbol;
  }

  public Optional<TimeFrame> getTimeFrame() {
    return Optional.ofNullable(timeFrame);
  }

  public void setTimeFrame(final TimeFrame timeFrame) {
    this.timeFrame = timeFrame;
  }

  public Optional<LocalDate> getFrom() {
    return Optional.ofNullable(from);
  }

  public void setFrom(final LocalDate from) {
    this.from = from;
  }

  public Optional<LocalDate> getTo() {
    return Optional.ofNullable(to);
  }

  public void setTo(final LocalDate to) {
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

  public FindRequest toFindRequest(final Clock clock) {
    return FindRequest.findSymbol(symbol)
        .timeframe(getTimeFrame().orElse(TimeFrame.ONE_DAY))
        .from(getFrom().orElse(LocalDate.now(clock).minusYears(2)))
        .to(getTo().orElse(LocalDate.now(clock)));
  }
}

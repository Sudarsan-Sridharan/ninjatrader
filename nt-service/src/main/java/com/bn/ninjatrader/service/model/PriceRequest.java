package com.bn.ninjatrader.service.model;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.request.FindRequest;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
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
public class PriceRequest {
  private static final Logger LOG = LoggerFactory.getLogger(PriceRequest.class);

  @PathParam("symbol")
  private String symbol;

  @QueryParam("timeframe")
  private TimeFrame timeFrame;

  @QueryParam("from")
  private LocalDate from;

  @QueryParam("to")
  private LocalDate to;

  public PriceRequest() {}

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

  public FindRequest toFindRequest(final Clock clock) {
    return FindRequest.findSymbol(symbol)
        .timeframe(getTimeFrame().orElse(TimeFrame.ONE_DAY))
        .from(getFrom().orElse(LocalDate.now(clock).minusYears(2)))
        .to(getTo().orElse(LocalDate.now(clock)));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PriceRequest that = (PriceRequest) o;
    return Objects.equal(symbol, that.symbol) &&
        timeFrame == that.timeFrame &&
        Objects.equal(from, that.from) &&
        Objects.equal(to, that.to);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(symbol, timeFrame, from, to);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("symbol", symbol)
        .add("timeFrame", timeFrame)
        .add("from", from)
        .add("to", to)
        .toString();
  }
}

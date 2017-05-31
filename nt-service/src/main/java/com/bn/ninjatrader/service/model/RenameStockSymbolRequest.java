package com.bn.ninjatrader.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bradwee2000@gmail.com
 */
public class RenameStockSymbolRequest {
  private static final Logger LOG = LoggerFactory.getLogger(RenameStockSymbolRequest.class);

  @JsonProperty("from")
  private String from;

  @JsonProperty("to")
  private String to;

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RenameStockSymbolRequest that = (RenameStockSymbolRequest) o;
    return Objects.equal(from, that.from) &&
        Objects.equal(to, that.to);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(from, to);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("from", from)
        .add("to", to)
        .toString();
  }
}

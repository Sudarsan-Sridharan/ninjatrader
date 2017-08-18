package com.bn.ninjatrader.process.adjustment;

import com.bn.ninjatrader.common.util.BasicIsoLocalDateDeserializer;
import com.bn.ninjatrader.common.util.BasicIsoLocalDateSerializer;
import com.bn.ninjatrader.common.model.Price;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Brad on 7/28/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceAdjustmentRequest {

  public static final Builder builder() {
    return new Builder();
  }

  @JsonProperty("symbol")
  private final String symbol;

  @JsonProperty("from")
  @JsonSerialize(using = BasicIsoLocalDateSerializer.class)
  private final LocalDate from;

  @JsonProperty("to")
  @JsonSerialize(using = BasicIsoLocalDateSerializer.class)
  private final LocalDate to;

  @JsonProperty("script")
  private final String script;

  public PriceAdjustmentRequest(@JsonProperty("symbol") final String symbol,
                                @JsonDeserialize(using = BasicIsoLocalDateDeserializer.class)
                                @JsonProperty("from") final LocalDate from,
                                @JsonDeserialize(using = BasicIsoLocalDateDeserializer.class)
                                @JsonProperty("to") final LocalDate to,
                                @JsonProperty("script") final String script) {
    this.symbol = symbol;
    this.from = from;
    this.to = to;
    this.script = script;
  }

  public String getScript() {
    return script;
  }

  public String getSymbol() {
    return symbol;
  }

  public LocalDate getFrom() {
    return from;
  }

  public LocalDate getTo() {
    return to;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PriceAdjustmentRequest request = (PriceAdjustmentRequest) o;
    return Objects.equal(symbol, request.symbol) &&
        Objects.equal(from, request.from) &&
        Objects.equal(to, request.to) &&
        Objects.equal(script, request.script);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(symbol, from, to, script);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("symbol", symbol)
        .add("from", from)
        .add("to", to)
        .add("script", script)
        .toString();
  }

  /**
   * Builder
   */
  public static class Builder<T extends Builder> {
    private String symbol;
    private LocalDate from;
    private LocalDate to;
    private String script;

    public T symbol(final String symbol) {
      this.symbol = symbol;
      return getThis();
    }

    public T from(final LocalDate from) {
      this.from = from;
      return getThis();
    }

    public T to(final LocalDate to) {
      this.to = to;
      return getThis();
    }

    public T script(final String script) {
      this.script = script;
      return getThis();
    }

    public PriceAdjustmentRequest build() {
      return new PriceAdjustmentRequest(symbol, from, to, script);
    }

    private T getThis() {
      return (T) this;
    }
  }

  public static class ExecutorBuilder extends Builder<ExecutorBuilder> {
    private PriceAdjustmentService priceAdjustmentService;

    public ExecutorBuilder(final PriceAdjustmentService priceAdjustmentService) {
      this.priceAdjustmentService = priceAdjustmentService;
    }

    public List<Price> execute() {
      return this.priceAdjustmentService.adjustPrices(build());
    }
  }
}

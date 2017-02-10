package com.bn.ninjatrader.process.adjustment;

import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.google.common.base.MoreObjects;

import java.time.LocalDate;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 7/28/16.
 */
public class PriceAdjustmentRequest {

  public static PriceAdjustmentRequest forSymbol(final String symbol) {
    return new PriceAdjustmentRequest(symbol);
  }

  private final String symbol;
  private LocalDate fromDate = LocalDate.now();
  private LocalDate toDate = LocalDate.now();
  private Operation operation;


  public PriceAdjustmentRequest(final String symbol) {
    checkNotNull(symbol, "symbol must not be null.");

    this.symbol = symbol;
  }

  public PriceAdjustmentRequest from(final LocalDate fromDate) {
    this.fromDate = fromDate;
    return this;
  }

  public PriceAdjustmentRequest to(final LocalDate toDate) {
    this.toDate = toDate;
    return this;
  }

  public PriceAdjustmentRequest adjustment(final Operation operation) {
    this.operation = operation;
    return this;
  }

  public String getSymbol() {
    return symbol;
  }

  public LocalDate getFromDate() {
    return fromDate;
  }

  public LocalDate getToDate() {
    return toDate;
  }

  public Operation getOperation() {
    return operation;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("symbol", symbol)
        .add("from", fromDate)
        .add("to", toDate)
        .add("operation", operation)
        .toString();
  }
}

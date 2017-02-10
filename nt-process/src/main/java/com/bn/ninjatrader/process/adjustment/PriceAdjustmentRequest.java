package com.bn.ninjatrader.process.adjustment;

import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 7/28/16.
 */
public class PriceAdjustmentRequest {

  public static PriceAdjustmentRequest forSymbol(final String symbol) {
    return new PriceAdjustmentRequest(symbol);
  }

  public static PriceAdjustmentRequest forSymbols(final Collection<String> symbols) {
    return new PriceAdjustmentRequest(symbols);
  }

  public static PriceAdjustmentRequest forSymbols(final String symbol, final String ... more) {
    return new PriceAdjustmentRequest(symbol, more);
  }

  public static PriceAdjustmentRequest forAllSymbols() {
    return new PriceAdjustmentRequest();
  }

  private final Set<String> symbols = Sets.newHashSet();
  private final boolean isForAllSymbols;
  private LocalDate fromDate = LocalDate.now();
  private LocalDate toDate = LocalDate.now();
  private Operation operation;

  public PriceAdjustmentRequest() {
    isForAllSymbols = true;
  }

  public PriceAdjustmentRequest(final String symbol) {
    this.symbols.add(symbol);
    isForAllSymbols = false;
  }

  public PriceAdjustmentRequest(final Collection<String> symbols) {
    checkNotNull(symbols, "symbols must not be null.");
    checkArgument(!symbols.isEmpty(), "symbols must not be empty.");

    isForAllSymbols = false;
    this.symbols.addAll(symbols);
  }

  public PriceAdjustmentRequest(final String symbol, final String ... more) {
    isForAllSymbols = false;
    this.symbols.addAll(Lists.asList(symbol, more));
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

  public boolean isForAllSymbols() {
    return isForAllSymbols;
  }

  public Set<String> getSymbols() {
    return symbols;
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
        .add("symbols", symbols)
        .add("from", fromDate)
        .add("to", toDate)
        .add("operation", operation)
        .toString();
  }
}

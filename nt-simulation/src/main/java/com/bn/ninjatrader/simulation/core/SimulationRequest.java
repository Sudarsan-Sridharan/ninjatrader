package com.bn.ninjatrader.simulation.core;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.time.LocalDate;

/**
 * @author bradwee2000@gmail.com
 */
public class SimulationRequest {

  public static final SimulationRequest withSymbol(final String symbol) {
    return new SimulationRequest(symbol);
  }

  private double startingCash;
  private String symbol;
  private LocalDate from;
  private LocalDate to;
  private String tradeAlgorithmId;

  private SimulationRequest(final String symbol) {
    this.symbol = symbol;
  }

  public SimulationRequest startingCash(final double startingCash) {
    this.startingCash = startingCash;
    return this;
  }

  public SimulationRequest from(final LocalDate from) {
    this.from = from;
    return this;
  }

  public SimulationRequest to(final LocalDate to) {
    this.to = to;
    return this;
  }

  public SimulationRequest tradeAlgorithmId(final String tradeAlgorithmId) {
    this.tradeAlgorithmId = tradeAlgorithmId;
    return this;
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

  public String getTradeAlgorithmId() {
    return tradeAlgorithmId;
  }

  public double getStartingCash() {
    return startingCash;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SimulationRequest that = (SimulationRequest) o;
    return Double.compare(that.startingCash, startingCash) == 0 &&
        Objects.equal(symbol, that.symbol) &&
        Objects.equal(from, that.from) &&
        Objects.equal(to, that.to) &&
        Objects.equal(tradeAlgorithmId, that.tradeAlgorithmId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(startingCash, symbol, from, to, tradeAlgorithmId);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("startingCash", startingCash)
        .add("symbol", symbol)
        .add("from", from)
        .add("to", to)
        .add("tradeAlgorithmId", tradeAlgorithmId)
        .toString();
  }
}

package com.bn.ninjatrader.simulation.scanner;

import com.bn.ninjatrader.common.model.Algorithm;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.Collection;
import java.util.Collections;

/**
 * @author bradwee2000@gmail.com
 */
public class ScanRequest {
  private static final int DEFAULT_DAYS = 30;

  public static final ScanRequest withAlgoId(final String algoId) {
    return new ScanRequest(algoId);
  }

  public static ScanRequest withAlgorithm(Algorithm algorithm) {
    return new ScanRequest(algorithm);
  }

  private Collection<String> symbols = Collections.emptyList(); // Empty means all symbols
  private String algoId;
  private Algorithm algorithm;
  private int days;

  private ScanRequest(final String algoId) {
    this.algoId = algoId;
    this.days = DEFAULT_DAYS;
  }

  private ScanRequest(final Algorithm algorithm) {
    this.algorithm = algorithm;
    this.days = DEFAULT_DAYS;
  }

  public ScanRequest days(final int days) {
    this.days = days;
    return this;
  }

  public ScanRequest allSymbols() {
    this.symbols = Collections.emptyList();
    return this;
  }

  public ScanRequest symbols(final Collection<String> symbols) {
    this.symbols = symbols;
    return this;
  }

  public int getDays() {
    return days;
  }

  public String getAlgorithmId() {
    return algoId;
  }

  public Algorithm getAlgorithm() {
    return algorithm;
  }

  public Collection<String> getSymbols() {
    return symbols;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ScanRequest that = (ScanRequest) o;
    return days == that.days &&
        Objects.equal(symbols, that.symbols) &&
        Objects.equal(algoId, that.algoId) &&
        Objects.equal(algorithm, that.algorithm);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(symbols, algoId, algorithm, days);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("symbols", symbols)
        .add("algoId", algoId)
        .add("algorithm", algorithm)
        .add("days", days)
        .toString();
  }
}

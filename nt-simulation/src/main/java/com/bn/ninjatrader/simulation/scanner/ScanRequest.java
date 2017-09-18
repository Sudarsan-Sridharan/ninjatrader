package com.bn.ninjatrader.simulation.scanner;

import com.bn.ninjatrader.common.model.Algorithm;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScanRequest {
  private static final int DEFAULT_DAYS = 30;

  public static final ScanRequest withAlgoId(final String algoId) {
    return new ScanRequest(algoId);
  }

  public static ScanRequest withAlgorithm(Algorithm algorithm) {
    return new ScanRequest(algorithm);
  }

  @JsonProperty("symbols")
  private Collection<String> symbols = Collections.emptyList(); // Empty means all symbols

  @JsonProperty("algorithmId")
  private String algorithmId;

  @JsonProperty("algorithm")
  private Algorithm algorithm;

  @JsonProperty("days")
  private int days;

  private ScanRequest() {}

  private ScanRequest(final String algorithmId) {
    this.algorithmId = algorithmId;
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

  public ScanRequest symbols(final String symbol, final String ... more) {
    this.symbols = Lists.asList(symbol, more);
    return this;
  }

  public int getDays() {
    return days;
  }

  public String getAlgorithmId() {
    return algorithm == null ? algorithmId : algorithm.getId();
  }

  @JsonIgnore
  public Optional<Algorithm> getAlgorithm() {
    return Optional.ofNullable(algorithm);
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
        Objects.equal(algorithmId, that.algorithmId) &&
        Objects.equal(algorithm, that.algorithm);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(symbols, algorithmId, algorithm, days);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("symbols", symbols)
        .add("algorithmId", algorithmId)
        .add("algorithm", algorithm)
        .add("days", days)
        .toString();
  }
}

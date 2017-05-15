package com.bn.ninjatrader.simulation.core;

import com.bn.ninjatrader.common.util.NtLocalDateDeserializer;
import com.bn.ninjatrader.common.util.NtLocalDateSerializer;
import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.algorithm.AlgorithmScript;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 8/3/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimulationParams {
  private static final Logger LOG = LoggerFactory.getLogger(SimulationParams.class);

  public static final Builder builder() {
    return new Builder();
  }

  @JsonProperty("from")
  @JsonSerialize(using = NtLocalDateSerializer.class)
  @JsonDeserialize(using = NtLocalDateDeserializer.class)
  private LocalDate fromDate;

  @JsonProperty("to")
  @JsonSerialize(using = NtLocalDateSerializer.class)
  @JsonDeserialize(using = NtLocalDateDeserializer.class)
  private LocalDate toDate;

  @JsonProperty("symbol")
  private String symbol;

  @JsonProperty("startingCash")
  private double startingCash;

  @JsonProperty("algorithm")
  private AlgorithmScript algorithm;

  @JsonIgnore
  private Set<Variable> variables;

  @JsonIgnore
  private Set<String> dataTypes;

  public SimulationParams() {}

  public SimulationParams(final LocalDate fromDate,
                          final LocalDate toDate,
                          final String symbol,
                          final double startingCash,
                          final AlgorithmScript algorithm) {
    this.fromDate = fromDate;
    this.toDate = toDate;
    this.symbol = symbol;
    this.startingCash = startingCash;
    this.algorithm = algorithm;
  }

  public LocalDate getFromDate() {
    return fromDate;
  }

  public void setFromDate(LocalDate fromDate) {
    this.fromDate = fromDate;
  }

  public LocalDate getToDate() {
    return toDate;
  }

  public void setToDate(LocalDate toDate) {
    this.toDate = toDate;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public double getStartingCash() {
    return startingCash;
  }

  public void setStartingCash(double startingCash) {
    this.startingCash = startingCash;
  }

  public AlgorithmScript getAlgorithm() {
    return algorithm;
  }

  @JsonIgnore
  public Set<Variable> getVariables() {
    checkNotNull(algorithm, "algorithm must not be null.");
    if (variables == null) {
      variables = Sets.newHashSet(algorithm.getVariables());
    }
    return variables;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SimulationParams params = (SimulationParams) o;
    return Double.compare(params.startingCash, startingCash) == 0 &&
        Objects.equal(fromDate, params.fromDate) &&
        Objects.equal(toDate, params.toDate) &&
        Objects.equal(symbol, params.symbol) &&
        Objects.equal(algorithm, params.algorithm) &&
        Objects.equal(variables, params.variables) &&
        Objects.equal(dataTypes, params.dataTypes);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(fromDate, toDate, symbol, startingCash, algorithm, variables, dataTypes);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("fromDate", fromDate)
        .add("toDate", toDate)
        .add("symbol", symbol)
        .add("startingCash", startingCash)
        .add("algorithm", algorithm)
        .add("variables", variables)
        .add("dataTypes", dataTypes)
        .toString();
  }

  /**
   * Builder class
   */
  public static final class Builder {
    private LocalDate fromDate;
    private LocalDate toDate;
    private String symbol;
    private double startingCash;
    private AlgorithmScript algorithm;

    public Builder from(final LocalDate fromDate) {
      this.fromDate = fromDate;
      return this;
    }
    public Builder to(final LocalDate toDate) {
      this.toDate = toDate;
      return this;
    }
    public Builder symbol(final String symbol) {
      this.symbol = symbol;
      return this;
    }
    public Builder startingCash(final double startingCash) {
      this.startingCash = startingCash;
      return this;
    }

    public Builder algorithm(final AlgorithmScript algorithm) {
      this.algorithm = algorithm;
      return this;
    }

    public SimulationParams build() {
      return new SimulationParams(fromDate, toDate, symbol, startingCash, algorithm);
    }
  }
}

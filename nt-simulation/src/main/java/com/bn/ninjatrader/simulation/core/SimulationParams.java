package com.bn.ninjatrader.simulation.core;

import com.bn.ninjatrader.common.util.NtLocalDateDeserializer;
import com.bn.ninjatrader.common.util.NtLocalDateSerializer;
import com.bn.ninjatrader.simulation.data.DataType;
import com.bn.ninjatrader.simulation.operation.Variable;
import com.bn.ninjatrader.simulation.statement.Statement;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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

  @JsonProperty("statements")
  private final List<Statement> statements = Lists.newArrayList();

  @JsonIgnore
  private final Set<Variable> variables = Sets.newHashSet();

  @JsonIgnore
  private final Set<DataType> dataTypes = Sets.newHashSet();

  public SimulationParams() {}

  public SimulationParams(final LocalDate fromDate,
                          final LocalDate toDate,
                          final String symbol,
                          final double startingCash,
                          final List<Statement> statements) {
    this.fromDate = fromDate;
    this.toDate = toDate;
    this.symbol = symbol;
    this.startingCash = startingCash;
    this.statements.addAll(statements);


    for (final Statement statement : statements) {
      variables.addAll(statement.getVariables());
    }
    for (final Variable variable : variables) {
      dataTypes.add(variable.getDataType());
    }
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

  public List<Statement> getStatements() {
    return Lists.newArrayList(statements);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof SimulationParams)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final SimulationParams rhs = (SimulationParams) obj;
    return Objects.equal(fromDate, rhs.fromDate)
        && Objects.equal(toDate, rhs.toDate)
        && Objects.equal(symbol, rhs.symbol)
        && Objects.equal(startingCash, rhs.startingCash)
        && Objects.equal(statements, rhs.statements);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(fromDate, toDate, symbol, startingCash, statements);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("fromDate", fromDate)
        .add("toDate", toDate)
        .add("symbol", symbol)
        .add("startingCash", startingCash)
        .add("statements", statements)
        .toString();
  }

  @JsonIgnore
  public Set<Variable> getVariables() {
    return variables;
  }

  @JsonIgnore
  public Set<DataType> getDataTypes() {
    return dataTypes;
  }

  /**
   * Builder class
   */
  public static final class Builder {
    private LocalDate fromDate;
    private LocalDate toDate;
    private String symbol;
    private double startingCash;
    private final List<Statement> statements = Lists.newArrayList();

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
    public Builder addStatement(final Statement statement) {
      this.statements.add(statement);
      return this;
    }
    public Builder addStatements(final Collection<Statement> statements) {
      this.statements.addAll(statements);
      return this;
    }
    public Builder addStatements(final Statement statement, final Statement ... more) {
      this.statements.addAll(Lists.asList(statement, more));
      return this;
    }
    public SimulationParams build() {
      return new SimulationParams(fromDate, toDate, symbol, startingCash, statements);
    }
  }
}

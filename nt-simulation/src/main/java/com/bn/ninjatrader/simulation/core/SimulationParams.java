package com.bn.ninjatrader.simulation.core;

import com.bn.ninjatrader.common.util.NtLocalDateDeserializer;
import com.bn.ninjatrader.common.util.NtLocalDateSerializer;
import com.bn.ninjatrader.simulation.condition.Condition;
import com.bn.ninjatrader.simulation.data.DataType;
import com.bn.ninjatrader.simulation.operation.Variable;
import com.bn.ninjatrader.simulation.order.BuyOrderParameters;
import com.bn.ninjatrader.simulation.order.SellOrderParameters;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Set;

/**
 * Created by Brad on 8/3/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimulationParams {
  private static final Logger LOG = LoggerFactory.getLogger(SimulationParams.class);

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

  @JsonProperty("buyCondition")
  private Condition buyCondition;

  @JsonProperty("sellCondition")
  private Condition sellCondition;

  @JsonProperty("buyOrderParams")
  private BuyOrderParameters buyOrderParams;

  @JsonProperty("sellOrderParams")
  private SellOrderParameters sellOrderParams;

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

  public Condition getBuyCondition() {
    return buyCondition;
  }

  public void setBuyCondition(Condition buyCondition) {
    this.buyCondition = buyCondition;
  }

  public Condition getSellCondition() {
    return sellCondition;
  }

  public void setSellCondition(Condition sellCondition) {
    this.sellCondition = sellCondition;
  }

  public BuyOrderParameters getBuyOrderParams() {
    return buyOrderParams;
  }

  public void setBuyOrderParams(BuyOrderParameters buyOrderParams) {
    this.buyOrderParams = buyOrderParams;
  }

  public SellOrderParameters getSellOrderParams() {
    return sellOrderParams;
  }

  public void setSellOrderParams(SellOrderParameters sellOrderParams) {
    this.sellOrderParams = sellOrderParams;
  }

  public Set<Variable> getVariables() {
    Set<Variable> variables = buyCondition.getVariables();
    variables.addAll(sellCondition.getVariables());
    return variables;
  }

  public Set<DataType> getDataTypes() {
    Set<DataType> dataTypes = Sets.newHashSet();
    for (Variable variable : getVariables()) {
      dataTypes.add(variable.getDataType());
    }
    return dataTypes;
  }
}

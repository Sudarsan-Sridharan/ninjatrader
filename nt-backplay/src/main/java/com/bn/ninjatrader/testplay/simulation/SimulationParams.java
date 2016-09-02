package com.bn.ninjatrader.testplay.simulation;

import com.bn.ninjatrader.testplay.simulation.data.DataType;
import com.bn.ninjatrader.testplay.condition.Condition;
import com.bn.ninjatrader.testplay.simulation.order.BuyOrderParameters;
import com.bn.ninjatrader.testplay.simulation.order.SellOrderParameters;

import java.time.LocalDate;
import java.util.Set;

/**
 * Created by Brad on 8/3/16.
 */
public class SimulationParams {

  private LocalDate fromDate;
  private LocalDate toDate;
  private String symbol;
  private double startingCash;
  private Condition buyCondition;
  private Condition sellCondition;
  private BuyOrderParameters buyOrderParams;
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

  public Set<DataType> getDataTypes() {
    Set<DataType> dataTypes = buyCondition.getDataTypes();
    dataTypes.addAll(sellCondition.getDataTypes());
    return dataTypes;
  }
}

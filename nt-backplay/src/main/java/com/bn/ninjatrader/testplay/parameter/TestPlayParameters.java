package com.bn.ninjatrader.testplay.parameter;

import com.bn.ninjatrader.testplay.condition.Condition;

import java.time.LocalDate;

/**
 * Created by Brad on 8/3/16.
 */
public class TestPlayParameters {

  private LocalDate fromDate;
  private LocalDate toDate;
  private String symbol;
  private double startingEquity;
  private Condition buyCondition;
  private Condition sellCondition;

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

  public double getStartingEquity() {
    return startingEquity;
  }

  public void setStartingEquity(double startingEquity) {
    this.startingEquity = startingEquity;
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
}

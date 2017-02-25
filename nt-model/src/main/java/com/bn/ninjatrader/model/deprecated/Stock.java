package com.bn.ninjatrader.model.deprecated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by Brad on 5/1/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Stock implements Comparable<Stock> {

  @JsonProperty("sym")
  private String symbol;

  @JsonProperty("nm")
  private String name;

  @JsonProperty("secId")
  private int securityId;

  public Stock() {}

  public Stock(String symbol, String name) {
    this(symbol, name, 0);
  }

  public Stock(String symbol, String name, int securityId) {
    this.symbol = symbol;
    this.name = name;
    this.securityId = securityId;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getSecurityId() {
    return securityId;
  }

  public void setSecurityId(int securityId) {
    this.securityId = securityId;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("symbol", symbol)
        .append("name", name)
        .append("securityId", securityId)
        .toString();
  }

  public int compareTo(Stock stock2) {
    return symbol.compareTo(stock2.getSymbol());
  }
}


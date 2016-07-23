package com.bn.ninjatrader.thirdparty.pse;

import com.bn.ninjatrader.common.data.Stock;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by Brad on 6/21/16.
 */
public class PseStock {

  @JsonProperty("securitySymbol")
  private String symbol;

  @JsonProperty("securityAlias")
  private String name;

  @JsonProperty("percChangeClose")
  private double pcntChangeClose;

  @JsonProperty("lastTradedPrice")
  private String close;

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

  public double getPcntChangeClose() {
    return pcntChangeClose;
  }

  public void setPcntChangeClose(double pcntChangeClose) {
    this.pcntChangeClose = pcntChangeClose;
  }

  public String getClose() {
    return close;
  }

  public void setClose(String close) {
    this.close = close;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("symbol", symbol)
        .append("name", name)
        .append("close", close)
        .append("pcntChange", pcntChangeClose)
        .toString();
  }

  public Stock toStock() {
    return new Stock(symbol, name);
  }
}

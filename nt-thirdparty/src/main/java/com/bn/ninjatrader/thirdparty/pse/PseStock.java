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

  @JsonProperty("totalVolume") // TODO Seems inaccurate.
  private String volume;

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

  public String getVolume() {
    return volume;
  }

  public void setVolume(String volume) {
    this.volume = volume;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("symbol", symbol)
        .append("name", name)
        .append("close", close)
        .append("pcntChange", pcntChangeClose)
        .append("volume", volume)
        .toString();
  }

  public Stock toStock() {
    return new Stock(symbol, name);
  }
}

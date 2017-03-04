package com.bn.ninjatrader.thirdparty.pse;

import com.bn.ninjatrader.model.deprecated.Stock;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

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

  @JsonProperty("securityName")
  public void setSecurityName(String name) {
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

  @JsonProperty("lastTradePrice")
  public void setLastTradePrice(String close) {
    this.close = close;
  }

  public String getVolume() {
    return volume;
  }

  public void setVolume(String volume) {
    this.volume = volume;
  }

  public Stock toStock() {
    return new Stock(symbol, name);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PseStock pseStock = (PseStock) o;
    return Double.compare(pseStock.pcntChangeClose, pcntChangeClose) == 0 &&
        Objects.equal(symbol, pseStock.symbol) &&
        Objects.equal(name, pseStock.name) &&
        Objects.equal(close, pseStock.close) &&
        Objects.equal(volume, pseStock.volume);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(symbol, name, pcntChangeClose, close, volume);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("symbol", symbol)
        .add("name", name)
        .add("pcntChangeClose", pcntChangeClose)
        .add("close", close)
        .add("volume", volume)
        .toString();
  }
}

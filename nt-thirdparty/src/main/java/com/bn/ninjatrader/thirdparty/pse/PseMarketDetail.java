package com.bn.ninjatrader.thirdparty.pse;

import com.bn.ninjatrader.thirdparty.pse.deserializer.PseLocalDateDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;

import java.time.LocalDate;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PseMarketDetail {

  @JsonProperty("tradingTime")
  @JsonDeserialize(using = PseLocalDateDeserializer.class)
  private LocalDate tradingTime;

  public LocalDate getTradingTime() {
    return tradingTime;
  }

  public void setTradingTime(LocalDate tradingTime) {
    this.tradingTime = tradingTime;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("tradingTime", tradingTime)
        .toString();
  }
}

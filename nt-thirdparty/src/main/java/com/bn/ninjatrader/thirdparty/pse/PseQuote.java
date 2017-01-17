package com.bn.ninjatrader.thirdparty.pse;

import com.bn.ninjatrader.common.data.DailyQuote;
import com.bn.ninjatrader.common.data.Price;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.IOException;


/**
 * Created by Brad on 6/21/16.
 */
public class PseQuote {

  @JsonProperty("securitySymbol")
  private String symbol;

  @JsonProperty("headerSqOpen")
  @JsonDeserialize(using = DoubleDeserializer.class)
  private double open;

  @JsonProperty("headerSqHigh")
  @JsonDeserialize(using = DoubleDeserializer.class)
  private double high;

  @JsonProperty("headerSqLow")
  @JsonDeserialize(using = DoubleDeserializer.class)
  private double low;

  @JsonProperty("headerLastTradePrice")
  @JsonDeserialize(using = DoubleDeserializer.class)
  private double close;

  @JsonProperty("headerTotalVolume")
  @JsonDeserialize(using = DoubleDeserializer.class)
  private double volume;

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public double getOpen() {
    return open;
  }

  public void setOpen(double open) {
    this.open = open;
  }

  public double getHigh() {
    return high;
  }

  public void setHigh(double high) {
    this.high = high;
  }

  public double getLow() {
    return low;
  }

  public void setLow(double low) {
    this.low = low;
  }

  public double getClose() {
    return close;
  }

  public void setClose(double close) {
    this.close = close;
  }

  public double getVolume() {
    return volume;
  }

  public void setVolume(double volume) {
    this.volume = volume;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("symbol", symbol)
        .append("O", open)
        .append("H", high)
        .append("L", low)
        .append("C", close)
        .append("V", volume)
        .toString();
  }

  public Price toPrice() {
    return Price.builder().open(open).high(high).low(low).close(close).volume((long) volume).build();
  }

  public DailyQuote toDailyQuote() {
    return new DailyQuote(symbol, null, open, high, low, close, (long) volume);
  }

  public static class Response extends ResponseResult<PseQuote> {

  }

  public static class DoubleDeserializer extends JsonDeserializer<Double> {
    @Override
    public Double deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      String valueAsString = p.getValueAsString();
      if (StringUtils.isEmpty(valueAsString)) {
        return null;
      }
      return Double.parseDouble(valueAsString.replaceAll(",", ""));
    }
  }
}

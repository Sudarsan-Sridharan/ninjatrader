package com.bn.ninjatrader.simulation.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by Brad on 8/29/16.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = BuyOrderParameters.class, name = "buy"),
    @JsonSubTypes.Type(value = SellOrderParameters.class, name = "sell")
})
public abstract class OrderParameters {

  public static BuyOrderParameters.BuyOrderParametersBuilder buy() {
    return new BuyOrderParameters.BuyOrderParametersBuilder();
  }

  public static SellOrderParameters.SellOrderParametersBuilder sell() {
    return new SellOrderParameters.SellOrderParametersBuilder();
  }

  @JsonProperty("marketTime")
  private MarketTime marketTime = MarketTime.CLOSE;

  @JsonProperty("barsFromNow")
  private int barsFromNow = 0;

  OrderParameters(MarketTime marketTime, int barsFromNow) {
    this.marketTime = marketTime;
    this.barsFromNow = barsFromNow;
  }

  public MarketTime getMarketTime() {
    return marketTime;
  }

  public void setMarketTime(MarketTime marketTime) {
    this.marketTime = marketTime;
  }

  public int getBarsFromNow() {
    return barsFromNow;
  }

  public void setBarsFromNow(int barsFromNow) {
    this.barsFromNow = barsFromNow;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) { return false; }
    if (!(obj instanceof OrderParameters)) {
      return false;
    }
    if (obj == this) { return true; }
    if (obj.getClass() != getClass()) {
      return false;
    }
    OrderParameters rhs = (OrderParameters) obj;
    return new EqualsBuilder()
        .append(marketTime, rhs.marketTime)
        .append(barsFromNow, rhs.barsFromNow)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(marketTime)
        .append(barsFromNow)
        .toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("marketTime", marketTime)
        .append("barsFromNow", barsFromNow)
        .toString();
  }

  /**
   * Abstract builder for OrderParameters.
   */
  public static abstract class OrderParametersBuilder<T> {
    private MarketTime marketTime = MarketTime.CLOSE;
    private int barsFromNow = 0;

    public T at(MarketTime marketTime) {
      this.marketTime = marketTime;
      return getThis();
    }

    public T barsFromNow(int barsFromNow) {
      this.barsFromNow = barsFromNow;
      return getThis();
    }

    public MarketTime getMarketTime() {
      return marketTime;
    }

    public int getBarsFromNow() {
      return barsFromNow;
    }

    public abstract T getThis();

    public abstract OrderParameters build();
  }
}

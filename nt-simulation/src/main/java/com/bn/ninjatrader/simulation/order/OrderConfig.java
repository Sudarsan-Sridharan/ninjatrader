package com.bn.ninjatrader.simulation.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderConfig {
  private static final int DEFAULT_BARS_FROM_NOW = 0;
  private static final int DEFAULT_EXPIRE_AFTER_NUM_OF_BARS = Integer.MAX_VALUE;
  private static final OrderConfig DEFAULT_CONFIG =
      new OrderConfig(DEFAULT_BARS_FROM_NOW, DEFAULT_EXPIRE_AFTER_NUM_OF_BARS);

  public static final OrderConfig defaults() {
    return DEFAULT_CONFIG;
  }

  public static final OrderConfig withBarsFromNow(final int barsFromNow) {
    return DEFAULT_CONFIG.barsFromNow(barsFromNow);
  }

  public static final OrderConfig withExpireAfterNumOfBars(final int expireAfterNumOfBars) {
    return DEFAULT_CONFIG.expireAfterNumOfBars(expireAfterNumOfBars);
  }

  @JsonProperty("barsFromNow")
  private final int barsFromNow; // wait for number of bars before attempting to fulfill order.

  @JsonProperty("expireAfterNumOfBars")
  private final int expireAfterNumOfBars; // Max number of bars to wait before marking as unfulfilled order as expired.

  public OrderConfig(@JsonProperty("barsFromNow") final int barsFromNow,
                     @JsonProperty("expireAfterNumOfBars") final int expireAfterNumOfBars) {
    checkArgument(expireAfterNumOfBars >= barsFromNow,
        "expireAfterNumOfBars must be >= barsFromNow. Otherwise it is always expired.");
    this.barsFromNow = barsFromNow;
    this.expireAfterNumOfBars = expireAfterNumOfBars;
  }

  public OrderConfig barsFromNow(final int barsFromNow) {
    return new OrderConfig(barsFromNow, this.expireAfterNumOfBars);
  }

  public OrderConfig expireAfterNumOfBars(final int expireAfterNumOfBars) {
    return new OrderConfig(this.barsFromNow, expireAfterNumOfBars);
  }

  public int getBarsFromNow() {
    return barsFromNow;
  }

  public int getExpireAfterNumOfBars() {
    return expireAfterNumOfBars;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof OrderConfig)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final OrderConfig rhs = (OrderConfig) obj;
    return Objects.equal(barsFromNow, rhs.barsFromNow)
        && Objects.equal(expireAfterNumOfBars, rhs.expireAfterNumOfBars);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(barsFromNow, expireAfterNumOfBars);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("barsFromNow", barsFromNow).add("expireAfterNumOfBars", expireAfterNumOfBars).toString();
  }
}

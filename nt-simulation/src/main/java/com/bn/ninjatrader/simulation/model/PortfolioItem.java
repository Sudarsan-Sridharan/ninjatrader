package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.common.util.NumUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * This class is immutable.
 *
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PortfolioItem {
  private static final Logger LOG = LoggerFactory.getLogger(PortfolioItem.class);

  public static final Builder builder() {
    return new Builder();
  }

  @JsonProperty("symbol")
  private final String symbol;

  @JsonProperty("totalShares")
  private final long totalShares;

  @JsonProperty("committedShares")
  private final long committedShares; // Shares committed to buy or sell orders.

  @JsonProperty("avgPrice")
  private final double avgPrice;

  public PortfolioItem(@JsonProperty("symbol") final String symbol,
                       @JsonProperty("totalShares") final long totalShares,
                       @JsonProperty("committedShares") final long committedShares,
                       @JsonProperty("avgPrice") final double avgPrice) {
    this.symbol = symbol;
    this.totalShares = totalShares;
    this.committedShares = committedShares;
    this.avgPrice = avgPrice;
  }

  /**
   * Add shares to portfolioItem.
   * This adjusts the average price of all the shares.
   * @param shares number of shares to add
   * @param price price of the new shares to add
   * @return new PortfolioItem with updated properties
   */
  public PortfolioItem addSharesWithPrice(final long shares, final double price) {
    checkArgument(shares > 0, "shares must be > 0");
    checkArgument(price > 0, "price must be > 0");

    final long newTotalShares = totalShares + shares;
    final double newAvgPrice = ((avgPrice * totalShares) + (price * shares)) / newTotalShares;
    return new PortfolioItem(symbol, newTotalShares, committedShares, newAvgPrice);
  }

  /**
   * Assumes committed shares are successfully sold.
   * Deducts from total shares and remaining committed shares.
   * @param committedShares number of committedShares to fullfill
   * @return new PortfolioItem with updated properties
   */
  public PortfolioItem fulfillCommittedShares(final long committedShares) {
    checkArgument(committedShares > 0, "shares must be > 0.");
    checkArgument(committedShares <= this.committedShares, "shares must be <= committedShares.");
    checkArgument(committedShares <= totalShares, "shares must be <= %s.", totalShares);
    final long newTotalShares = totalShares - committedShares;
    final long newCommittedShares = this.committedShares - committedShares;
    return new PortfolioItem(symbol, newTotalShares, newCommittedShares, avgPrice);
  }

  /**
   * Reserves number of shares for selling.
   * @param shares
   * @return new PortfolioItem with updated properties.
   */
  public PortfolioItem commitShares(final long shares) {
    checkArgument(shares > 0, "shares must be > 0.");
    checkArgument(committedShares + shares <= totalShares, "Cannot commit more total available shares.");
    final long newCommittedShares = committedShares + shares;
    return new PortfolioItem(symbol, totalShares, newCommittedShares, avgPrice);
  }

  /**
   * Check if it can set aside number of shares to be sold.
   * @param shares
   * @return true if it can set aside number of shares to be sold.
   */
  public boolean canCommitShares(final long shares) {
    checkArgument(shares > 0, "shares must be > 0.");
    return committedShares + shares <= totalShares;
  }

  /**
   * Cancels committed shares.
   * @param shares
   * @return new PortfolioItem with updated properties.
   */
  public PortfolioItem cancelCommittedShares(final long shares) {
    checkArgument(shares > 0, "shares must be > 0.");
    checkArgument(shares <= committedShares, "Cannot cancel more than committedShares.");
    final long newCommittedShares = committedShares - shares;
    return new PortfolioItem(symbol, totalShares, newCommittedShares, avgPrice);
  }

  public String getSymbol() {
    return symbol;
  }

  public long getTotalShares() {
    return totalShares;
  }

  public long getCommittedShares() {
    return committedShares;
  }

  public double getAvgPrice() {
    return avgPrice;
  }

  public double getEquityValue() {
    return NumUtil.multiply(totalShares, avgPrice);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof PortfolioItem)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final PortfolioItem rhs = (PortfolioItem) obj;
    return Objects.equal(symbol, rhs.symbol)
        && Objects.equal(totalShares, rhs.totalShares)
        && Objects.equal(committedShares, rhs.committedShares)
        && Objects.equal(avgPrice, rhs.avgPrice);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(symbol, totalShares, committedShares, avgPrice);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("symbol", symbol)
        .add("totalShares", totalShares)
        .add("committedShares", committedShares)
        .add("avgPrice", avgPrice)
        .toString();
  }

  /**
   * Builder class
   */
  public static final class Builder {
    private String symbol;
    private long totalShares;
    private long committedShares;
    private double avgPrice;

    public Builder symbol(final String symbol) {
      this.symbol = symbol;
      return this;
    }

    public Builder totalShares(final long totalShares) {
      this.totalShares = totalShares;
      return this;
    }

    public Builder committedShares(final long committedShares) {
      this.committedShares = committedShares;
      return this;
    }

    public Builder avgPrice(final double avgPrice) {
      this.avgPrice = avgPrice;
      return this;
    }

    public PortfolioItem build() {
      return new PortfolioItem(symbol, totalShares, committedShares, avgPrice);
    }
  }
}

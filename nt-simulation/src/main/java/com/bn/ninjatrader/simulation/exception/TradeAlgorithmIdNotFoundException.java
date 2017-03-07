package com.bn.ninjatrader.simulation.exception;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author bradwee2000@gmail.com
 */
public class TradeAlgorithmIdNotFoundException extends RuntimeException {
  private static final String ERROR_MSG = "TradeAlgorithm with id [%s] is not found.";
  private final String tradeAlgorithmId;

  public TradeAlgorithmIdNotFoundException(final String tradeAlgorithmId) {
    super(String.format(ERROR_MSG, tradeAlgorithmId));
    this.tradeAlgorithmId = tradeAlgorithmId;
  }

  public String getTradeAlgorithmId() {
    return tradeAlgorithmId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TradeAlgorithmIdNotFoundException that = (TradeAlgorithmIdNotFoundException) o;
    return Objects.equal(tradeAlgorithmId, that.tradeAlgorithmId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(tradeAlgorithmId);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("tradeAlgorithmId", tradeAlgorithmId)
        .toString();
  }
}

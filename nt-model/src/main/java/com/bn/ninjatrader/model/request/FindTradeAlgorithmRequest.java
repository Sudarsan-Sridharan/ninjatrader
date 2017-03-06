package com.bn.ninjatrader.model.request;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author bradwee2000@gmail.com
 */
public class FindTradeAlgorithmRequest {
  public static final FindTradeAlgorithmRequest withTradeAlgorithmId(final String tradeAlgorithmId) {
    return new FindTradeAlgorithmRequest(tradeAlgorithmId, null);
  }

  public static final FindTradeAlgorithmRequest withUserId(final String userId) {
    return new FindTradeAlgorithmRequest(null, userId);
  }

  private final String tradeAlgorithmId;
  private final String userId;

  private FindTradeAlgorithmRequest(final String tradeAlgorithmId, final String userId) {
    this.tradeAlgorithmId = tradeAlgorithmId;
    this.userId = userId;
  }

  public String getTradeAlgorithmId() {
    return tradeAlgorithmId;
  }

  public String getUserId() {
    return userId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FindTradeAlgorithmRequest that = (FindTradeAlgorithmRequest) o;
    return Objects.equal(tradeAlgorithmId, that.tradeAlgorithmId) &&
        Objects.equal(userId, that.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(tradeAlgorithmId, userId);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("tradeAlgorithmId", tradeAlgorithmId)
        .add("userId", userId)
        .toString();
  }
}

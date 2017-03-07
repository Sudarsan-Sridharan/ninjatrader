package com.bn.ninjatrader.simulation.service;

import com.bn.ninjatrader.simulation.model.SimTradeAlgorithm;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author bradwee2000@gmail.com
 */
public class SaveSimTradeAlgoRequest {

  public static final SaveSimTradeAlgoRequest withAlgorithm(final SimTradeAlgorithm algo) {
    return new SaveSimTradeAlgoRequest().algorithm(algo);
  }

  private SimTradeAlgorithm simTradeAlgorithm;
  private String tradeAlgorithmId;
  private String userId;
  private String description;

  private SaveSimTradeAlgoRequest() {}

  public SaveSimTradeAlgoRequest algorithm(final SimTradeAlgorithm algo) {
    this.simTradeAlgorithm = algo;
    return this;
  }

  public SaveSimTradeAlgoRequest tradeAlgorithmId(final String algoId) {
    this.tradeAlgorithmId = algoId;
    return this;
  }

  public SaveSimTradeAlgoRequest userId(final String userId) {
    this.userId = userId;
    return this;
  }

  public SaveSimTradeAlgoRequest description(final String description) {
    this.description = description;
    return this;
  }

  public SimTradeAlgorithm getSimTradeAlgorithm() {
    return simTradeAlgorithm;
  }

  public String getTradeAlgorithmId() {
    return tradeAlgorithmId;
  }

  public String getUserId() {
    return userId;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SaveSimTradeAlgoRequest that = (SaveSimTradeAlgoRequest) o;
    return Objects.equal(simTradeAlgorithm, that.simTradeAlgorithm) &&
        Objects.equal(tradeAlgorithmId, that.tradeAlgorithmId) &&
        Objects.equal(userId, that.userId) &&
        Objects.equal(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(simTradeAlgorithm, tradeAlgorithmId, userId, description);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("simTradeAlgorithm", simTradeAlgorithm)
        .add("tradeAlgorithmId", tradeAlgorithmId)
        .add("userId", userId)
        .add("description", description)
        .toString();
  }
}

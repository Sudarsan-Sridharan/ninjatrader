package com.bn.ninjatrader.simulation.exception;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author bradwee2000@gmail.com
 */
public class AlgorithmNotFoundException extends RuntimeException {
  private static final String ERROR_MSG = "Algorithm with id [%s] is not found.";
  private final String algorithmId;

  public AlgorithmNotFoundException(final String algorithmId) {
    super(String.format(ERROR_MSG, algorithmId));
    this.algorithmId = algorithmId;
  }

  public String getAlgorithmId() {
    return algorithmId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AlgorithmNotFoundException that = (AlgorithmNotFoundException) o;
    return Objects.equal(algorithmId, that.algorithmId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(algorithmId);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("algorithmId", algorithmId)
        .toString();
  }
}

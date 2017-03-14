package com.bn.ninjatrader.simulation.scanner;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author bradwee2000@gmail.com
 */
public class ScanRequest {
  private static final int DEFAULT_DAYS = 4;

  public static final ScanRequest withAlgoId(final String algoId) {
    return new ScanRequest(algoId);
  }

  private String algoId;
  private int days;

  private ScanRequest(final String algoId) {
    this.algoId = algoId;
    this.days = DEFAULT_DAYS;
  }

  public ScanRequest days(final int days) {
    this.days = days;
    return this;
  }

  public int getDays() {
    return days;
  }

  public String getAlgoId() {
    return algoId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ScanRequest that = (ScanRequest) o;
    return days == that.days &&
        Objects.equal(algoId, that.algoId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(algoId, days);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("algoId", algoId)
        .add("days", days)
        .toString();
  }
}

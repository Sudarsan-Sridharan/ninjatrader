package com.bn.ninjatrader.model.request;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.Optional;

/**
 * @author bradwee2000@gmail.com
 */
public class FindReportRequest {
  public static final FindReportRequest withReportId(final String reportId) {
    return new FindReportRequest(reportId);
  }

  private final Optional<String> reportId;

  private FindReportRequest(final String reportId) {
    this.reportId = Optional.ofNullable(reportId);
  }

  public Optional<String> getReportId() {
    return reportId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FindReportRequest that = (FindReportRequest) o;
    return Objects.equal(reportId, that.reportId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(reportId);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("reportId", reportId)
        .toString();
  }
}

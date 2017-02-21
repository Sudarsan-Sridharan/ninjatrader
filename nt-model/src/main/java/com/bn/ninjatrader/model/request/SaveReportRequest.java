package com.bn.ninjatrader.model.request;

import com.bn.ninjatrader.common.data.Report;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
public class SaveReportRequest {
  public static final SaveReportRequest save(final Report report) {
    return new SaveReportRequest(report);
  }
  public static final SaveReportRequest save(final Collection<Report> reports) {
    return new SaveReportRequest(reports);
  }
  public static final SaveReportRequest save(final Report report, final Report ... more) {
    return new SaveReportRequest(Lists.asList(report, more));
  }

  private final List<Report> reports = Lists.newArrayList();

  private SaveReportRequest(final Report report) {
    this.reports.add(report);
  }

  private SaveReportRequest(final Collection<Report> reports) {
    this.reports.addAll(reports);
  }

  public List<Report> getReports() {
    return Lists.newArrayList(reports);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final SaveReportRequest that = (SaveReportRequest) o;
    return Objects.equal(reports, that.reports);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(reports);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("reports", reports)
        .toString();
  }
}

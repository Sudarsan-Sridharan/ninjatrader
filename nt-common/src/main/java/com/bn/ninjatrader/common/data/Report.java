package com.bn.ninjatrader.common.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.time.LocalDateTime;

/**
 * Created by Brad on 7/28/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Report {

  @JsonProperty("reportId")
  private String reportId;

  @JsonProperty("userId")
  private String userId;

  @JsonProperty("data")
  private Object data;

  @JsonProperty("lastUpdateDate")
  private LocalDateTime lastUpdateDate;

  public Report() {}

  public Report(final String reportId, final String userId, final String data) {
    this.reportId = reportId;
    this.userId = userId;
    this.data = data;
  }

  public String getReportId() {
    return reportId;
  }

  public void setReportId(String reportId) {
    this.reportId = reportId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public LocalDateTime getLastUpdateDate() {
    return lastUpdateDate;
  }

  public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
    this.lastUpdateDate = lastUpdateDate;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("reportId", reportId).add("user", userId).add("data", data).add("lastUpdateDate", lastUpdateDate)
        .toString();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(reportId, userId, data, lastUpdateDate);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof Report)) { return false; }
    if (obj == this) { return true; }
    final Report rhs = (Report) obj;
    return Objects.equal(reportId, rhs.reportId)
      && Objects.equal(userId, rhs.userId)
      && Objects.equal(data, rhs.data)
      && Objects.equal(lastUpdateDate, rhs.lastUpdateDate);
  }
}


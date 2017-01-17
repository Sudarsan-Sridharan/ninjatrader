package com.bn.ninjatrader.common.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;

/**
 * Created by Brad on 7/28/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Report {

  @JsonProperty("report_id")
  private String reportId;

  @JsonProperty("user")
  private String user;

  @JsonProperty("data")
  private Object data;

  @JsonProperty("lud")
  private LocalDateTime lastUpdateDate;

  public Report() {}

  public Report(String reportId, String user, String data) {
    this.reportId = reportId;
    this.user = user;
    this.data = data;
  }

  public String getReportId() {
    return reportId;
  }

  public void setReportId(String reportId) {
    this.reportId = reportId;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
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
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("reportId", reportId)
        .append("user", user)
        .append("data", data)
        .append("lastUpdateDate", lastUpdateDate)
        .toString();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(reportId)
        .append(user)
        .append(data)
        .append(lastUpdateDate)
        .toHashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) { return false; }
    if (!(obj instanceof Report)) {
      return false;
    }
    if (obj == this) { return true; }
    if (obj.getClass() != getClass()) {
      return false;
    }

    Report rhs = (Report) obj;
    return new EqualsBuilder()
        .append(reportId, rhs.reportId)
        .append(user, rhs.user)
        .append(data, rhs.data)
        .append(lastUpdateDate, rhs.lastUpdateDate)
        .isEquals();
  }
}


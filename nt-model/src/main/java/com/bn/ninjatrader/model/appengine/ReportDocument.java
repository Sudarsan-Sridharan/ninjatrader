package com.bn.ninjatrader.model.appengine;

import com.google.appengine.api.datastore.Text;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;

/**
 * @author bradwee2000@gmail.com
 */
@Entity
public class ReportDocument {

  public static final String id(final String userId, final String reportId) {
    return String.format("%s-%s", userId, reportId);
  }

  @Id
  private String id;

  @Index
  private String userId;

  @Index
  private String reportId;

  @Unindex
  private Text data;

  private ReportDocument() {}

  public ReportDocument(final String userId, final String reportId, final String jsonReport) {
    this.id = id(userId, reportId);
    this.userId = userId;
    this.reportId = reportId;
    this.data = new Text(jsonReport);
  }

  public String getId() {
    return id;
  }

  public String getUserId() {
    return userId;
  }

  public String getReportId() {
    return reportId;
  }

  public Text getData() {
    return data;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ReportDocument that = (ReportDocument) o;
    return Objects.equal(id, that.id) &&
        Objects.equal(userId, that.userId) &&
        Objects.equal(reportId, that.reportId) &&
        Objects.equal(data, that.data);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, userId, reportId, data);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("userId", userId)
        .add("reportId", reportId)
        .add("data", data)
        .toString();
  }
}

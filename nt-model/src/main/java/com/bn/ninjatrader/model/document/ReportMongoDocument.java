package com.bn.ninjatrader.model.document;

import com.bn.ninjatrader.common.data.Report;
import com.bn.ninjatrader.model.util.QueryParam;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

/**
 * Created by Brad on 11/7/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportMongoDocument {

  @MongoId
  @MongoObjectId
  private String id;

  @JsonProperty(QueryParam.DATA)
  private Report report;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Report getReport() {
    return report;
  }

  public void setReport(Report report) {
    this.report = report;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("report", report)
        .toString();
  }
}

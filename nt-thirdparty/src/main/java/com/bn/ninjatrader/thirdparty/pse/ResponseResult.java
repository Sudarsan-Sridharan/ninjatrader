package com.bn.ninjatrader.thirdparty.pse;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * Created by Brad on 6/21/16.
 */
public class ResponseResult<T> {

  @JsonProperty("records")
  private List<T> records;

  public List<T> getRecords() {
    return records;
  }

  public void setRecords(List<T> records) {
    this.records = records;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("records", records)
        .toString();
  }
}

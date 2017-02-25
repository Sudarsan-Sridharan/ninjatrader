package com.bn.ninjatrader.service.model;

import com.bn.ninjatrader.model.deprecated.Ichimoku;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * Created by Brad on 5/28/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IchimokuResponse {

  @JsonProperty("values")
  private List<Ichimoku> values = Lists.newArrayList();

  public List<Ichimoku> getValues() {
    return values;
  }

  public IchimokuResponse setValues(List<Ichimoku> values) {
    this.values = values;
    return this;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("values", values)
        .toString();
  }
}

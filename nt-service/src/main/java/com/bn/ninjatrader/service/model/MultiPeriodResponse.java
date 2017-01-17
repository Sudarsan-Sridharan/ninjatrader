package com.bn.ninjatrader.service.model;

import com.bn.ninjatrader.common.data.Value;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * Created by Brad on 5/28/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MultiPeriodResponse<T extends Value> {

  @JsonProperty("values")
  private Multimap<Integer, T> values = LinkedHashMultimap.create();

  public MultiPeriodResponse put(int period, List<T> newValues) {
    values.putAll(period, newValues);
    return this;
  }

  public Multimap<Integer, T> getValues() {
    return values;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("values", values)
        .toString();
  }
}

package com.bn.ninjatrader.service.model;

import com.bn.ninjatrader.common.data.Value;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import java.util.List;

/**
 * Created by Brad on 5/28/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MultiPeriodResponse<T extends Value> {

  @JsonProperty("values")
  private Multimap<Integer, T> values = LinkedHashMultimap.create();

  public MultiPeriodResponse put(final int period, final List<T> newValues) {
    values.putAll(period, newValues);
    return this;
  }

  public Multimap<Integer, T> getValues() {
    return values;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("values", values)
        .toString();
  }
}

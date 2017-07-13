package com.bn.ninjatrader.service.model;

import com.bn.ninjatrader.common.util.NtLocalDateDeserializer;
import com.bn.ninjatrader.common.util.NtLocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImportQuotesRequest {

  @JsonSerialize(contentUsing = NtLocalDateSerializer.class)
  @JsonDeserialize(contentUsing = NtLocalDateDeserializer.class)
  private List<LocalDate> dates = Collections.emptyList();

  public List<LocalDate> getDates() {
    return dates;
  }

  public void setDates(List<LocalDate> dates) {
    this.dates = dates;
    if (this.dates == null) {
      this.dates = Collections.emptyList();
    }
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("dates", dates)
        .toString();
  }
}

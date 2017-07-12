package com.bn.ninjatrader.service.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
public class ImportQuotesRequest {

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
}

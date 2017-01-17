package com.bn.ninjatrader.thirdparty.pse;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Brad on 11/04/16.
 */
public class PseAllStockUpdate {

  private LocalDateTime lastUpdateDate;

  private List<PseStock> stocks;

  public PseAllStockUpdate(LocalDateTime lastUpdateDate, List<PseStock> stocks) {
    this.lastUpdateDate = lastUpdateDate;
    this.stocks = stocks;
  }

  public LocalDateTime getLastUpdateDate() {
    return lastUpdateDate;
  }

  public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
    this.lastUpdateDate = lastUpdateDate;
  }

  public List<PseStock> getStocks() {
    return stocks;
  }

  public void setStocks(List<PseStock> stocks) {
    this.stocks = stocks;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("lastUpdateDate", lastUpdateDate)
        .append("stocks", stocks)
        .toString();
  }
}

package com.bn.ninjatrader.thirdparty.pse;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * Created by Brad on 6/21/16.
 */
public class PseSecurity {

  @JsonProperty("symbol")
  private String symbol;

  @JsonProperty("securityId")
  private int securityId;

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public int getSecurityId() {
    return securityId;
  }

  public void setSecurityId(int securityId) {
    this.securityId = securityId;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("symbol", symbol)
        .append("securityId", securityId)
        .toString();
  }

  public static class Response extends ResponseResult<PseSecurity> {

//    @JsonProperty("records")
//    private List<Security> records;
//
//    public List<Security> getRecords() {
//      return records;
//    }
//
//    public void setRecords(List<Security> records) {
//      this.records = records;
//    }
//
//    @Override
//    public String toString() {
//      return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
//          .append("records", records)
//          .toString();
//    }
  }
}

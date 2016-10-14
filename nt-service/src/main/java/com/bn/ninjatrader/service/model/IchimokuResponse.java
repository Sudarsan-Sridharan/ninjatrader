package com.bn.ninjatrader.service.model;

import com.bn.ninjatrader.common.data.Price;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Brad on 5/28/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IchimokuResponse {

  @JsonProperty("from")
  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  private LocalDate fromDate;

  @JsonProperty("to")
  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  private LocalDate toDate;

  @JsonProperty("summary")
  private Price priceSummary;

  @JsonProperty("prices")
  private List<Price> priceList = Lists.newArrayList();

  public LocalDate getFromDate() {
    return fromDate;
  }

  public void setFromDate(LocalDate fromDate) {
    this.fromDate = fromDate;
  }

  public LocalDate getToDate() {
    return toDate;
  }

  public void setToDate(LocalDate toDate) {
    this.toDate = toDate;
  }

  public Price getPriceSummary() {
    return priceSummary;
  }

  public void setPriceSummary(Price priceSummary) {
    this.priceSummary = priceSummary;
  }

  public List<Price> getPriceList() {
    return priceList;
  }

  public void setPriceList(List<Price> priceList) {
    this.priceList = priceList;
  }

  @JsonProperty("size")
  public int getSize() {
    return priceList.size();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("fromDate", fromDate)
        .append("toDate", toDate)
        .append("priceSummary", priceSummary)
        .append("size", priceList.size())
        .toString();
  }
}

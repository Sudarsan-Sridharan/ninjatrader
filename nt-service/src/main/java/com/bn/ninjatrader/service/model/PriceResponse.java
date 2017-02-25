package com.bn.ninjatrader.service.model;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.common.util.NtLocalDateDeserializer;
import com.bn.ninjatrader.common.util.NtLocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Brad on 5/28/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceResponse {

  @JsonProperty("from")
  @JsonSerialize(using = NtLocalDateSerializer.class)
  @JsonDeserialize(using = NtLocalDateDeserializer.class)
  private LocalDate fromDate;

  @JsonProperty("to")
  @JsonSerialize(using = NtLocalDateSerializer.class)
  @JsonDeserialize(using = NtLocalDateDeserializer.class)
  private LocalDate toDate;

  @JsonProperty("values")
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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PriceResponse that = (PriceResponse) o;
    return Objects.equal(fromDate, that.fromDate) &&
        Objects.equal(toDate, that.toDate) &&
        Objects.equal(priceList, that.priceList);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(fromDate, toDate, priceList);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("fromDate", fromDate)
        .add("toDate", toDate)
        .add("priceList", priceList)
        .toString();
  }
}

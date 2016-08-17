package com.bn.ninjatrader.model.data;

import com.bn.ninjatrader.model.util.QueryParamName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import java.util.List;

/**
 * Created by Brad on 6/3/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractStockData<T> implements Comparable<AbstractStockData> {

  public static final String SYMBOL = "sym";
  public static final String YEAR = "yr";
  public static final String DATA = "data";

  @MongoId
  @MongoObjectId
  public String id;

  @JsonProperty(QueryParamName.SYMBOL)
  private String symbol;

  @JsonProperty(QueryParamName.YEAR)
  private int year;

  @JsonProperty(QueryParamName.DATA)
  private List<T> data = Lists.newArrayList();

  public AbstractStockData() {}

  public AbstractStockData(String symbol, int year) {
    this.symbol = symbol;
    this.year = year;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public List<T> getData() {
    return data;
  }

  public void setData(List<T> data) {
    this.data = data;
  }

  public boolean isEmpty() {
    return data == null || data.isEmpty();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("symbol", symbol)
        .append("year", year)
        .append("data", data)
        .build();
  }

  public int compareTo(AbstractStockData data2) {
    if (!symbol.equals(data2.getSymbol())) {
      return symbol.compareTo(data2.getSymbol());
    }
    return year - data2.getYear();
  }
}

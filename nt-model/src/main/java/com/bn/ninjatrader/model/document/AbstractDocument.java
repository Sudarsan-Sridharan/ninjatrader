package com.bn.ninjatrader.model.document;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.util.QueryParam;
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
public abstract class AbstractDocument<T> implements Comparable<AbstractDocument> {

  @MongoId
  @MongoObjectId
  public String id;

  @JsonProperty(QueryParam.SYMBOL)
  private String symbol;

  @JsonProperty(QueryParam.TIMEFRAME)
  private TimeFrame timeFrame;

  @JsonProperty(QueryParam.YEAR)
  private int year;

  @JsonProperty(QueryParam.DATA)
  private List<T> data = Lists.newArrayList();

  public AbstractDocument() {}

  public AbstractDocument(String symbol, int year) {
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

  public TimeFrame getTimeFrame() {
    return timeFrame;
  }

  public void setTimeFrame(TimeFrame timeFrame) {
    this.timeFrame = timeFrame;
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
        .append("timeFrame", timeFrame)
        .append("year", year)
        .append("data", data)
        .build();
  }

  public int compareTo(AbstractDocument data2) {
    if (!symbol.equals(data2.getSymbol())) {
      return symbol.compareTo(data2.getSymbol());
    }
    if (!timeFrame.equals(data2.getTimeFrame())) {
      return timeFrame.compareTo(data2.getTimeFrame());
    }
    return year - data2.getYear();
  }
}

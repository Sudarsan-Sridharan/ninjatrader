package com.bn.ninjatrader.model.mongo.document;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.mongo.util.QueryParam;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.googlecode.objectify.annotation.Id;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import java.util.List;

/**
 * Created by Brad on 6/3/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractDocument<T> implements Comparable<AbstractDocument> {

  public static final String id(final String symbol, final int year, final TimeFrame timeFrame) {
    return String.format("%s-%s-%s", symbol, year, timeFrame);
  }

  @MongoId
  @MongoObjectId
  public String mongoId;

  @Id
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

  public AbstractDocument(final String symbol, final int year) {
    this.symbol = symbol;
    this.year = year;
  }

  public AbstractDocument(final String symbol, final int year, final TimeFrame timeFrame) {
    this.id = id(symbol, year, timeFrame);
    this.symbol = symbol;
    this.year = year;
    this.timeFrame = timeFrame;
  }

  public String getId() {
    return mongoId;
  }

  public void setId(String id) {
    this.mongoId = id;
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
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("symbol", symbol)
        .add("timeFrame", timeFrame)
        .add("year", year)
        .add("data", data)
        .toString();
  }

  public int compareTo(final AbstractDocument data2) {
    if (!symbol.equals(data2.getSymbol())) {
      return symbol.compareTo(data2.getSymbol());
    }
    if (!timeFrame.equals(data2.getTimeFrame())) {
      return timeFrame.compareTo(data2.getTimeFrame());
    }
    return year - data2.getYear();
  }
}

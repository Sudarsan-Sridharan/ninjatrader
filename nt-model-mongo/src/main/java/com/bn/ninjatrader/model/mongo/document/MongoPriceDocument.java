package com.bn.ninjatrader.model.mongo.document;

import com.bn.ninjatrader.common.model.Price;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.mongo.util.QueryParam;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.googlecode.objectify.annotation.Entity;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import java.util.List;

/**
 * Created by Brad on 6/3/16.
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class MongoPriceDocument implements Comparable<MongoPriceDocument> {

  public static final String id(final String symbol, final int year, final TimeFrame timeFrame) {
    return String.format("%s-%s-%s", symbol, year, timeFrame);
  }

  @MongoId
  @MongoObjectId
  private String mongoId;

  @JsonProperty(QueryParam.SYMBOL)
  private String symbol;

  @JsonProperty(QueryParam.TIMEFRAME)
  private TimeFrame timeFrame;

  @JsonProperty(QueryParam.YEAR)
  private int year;

  @JsonProperty(QueryParam.DATA)
  private List<Price> data = Lists.newArrayList();

  public MongoPriceDocument() {}

  public MongoPriceDocument(final String symbol, final int year) {
    this.symbol = symbol;
    this.year = year;
  }

  public MongoPriceDocument(final String symbol, final int year, final TimeFrame timeFrame) {
    this.symbol = symbol;
    this.year = year;
    this.timeFrame = timeFrame;
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

  public List<Price> getData() {
    return data;
  }

  public void setData(List<Price> data) {
//    this.data = data;
  }

  @Override
  public int compareTo(final MongoPriceDocument other) {
    if (!symbol.equals(other.symbol)) {
      return symbol.compareTo(other.symbol);
    }
    if (!timeFrame.equals(other.timeFrame)) {
      return timeFrame.compareTo(other.timeFrame);
    }
    return year - other.year;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("mongoId", mongoId)
        .add("symbol", symbol)
        .add("timeFrame", timeFrame)
        .add("year", year)
        .add("data", data)
        .toString();
  }
}

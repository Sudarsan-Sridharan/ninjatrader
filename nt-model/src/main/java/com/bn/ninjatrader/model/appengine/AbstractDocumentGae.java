package com.bn.ninjatrader.model.appengine;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.googlecode.objectify.annotation.Index;

import java.util.List;

/**
 * Created by Brad on 6/3/16.
 */
public abstract class AbstractDocumentGae<T> implements Comparable<AbstractDocumentGae> {

  @Index
  private String symbol;

  @Index
  private TimeFrame timeFrame;

  @Index
  private int year;

  private List<T> data = Lists.newArrayList();

  public AbstractDocumentGae() {}

  public AbstractDocumentGae(final String symbol, final int year, final TimeFrame timeFrame) {
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
        .add("symbol", symbol)
        .add("timeFrame", timeFrame)
        .add("year", year)
        .add("data", data)
        .toString();
  }

  public int compareTo(final AbstractDocumentGae data2) {
    if (!symbol.equals(data2.getSymbol())) {
      return symbol.compareTo(data2.getSymbol());
    }
    if (!timeFrame.equals(data2.getTimeFrame())) {
      return timeFrame.compareTo(data2.getTimeFrame());
    }
    return year - data2.getYear();
  }
}

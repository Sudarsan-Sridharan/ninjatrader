package com.bn.ninjatrader.model.datastore.document;

import com.bn.ninjatrader.common.model.Price;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.datastore.entity.DatastorePrice;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Brad on 6/3/16.
 */
@Entity
public class PriceDocument {

  private static final String ID_TEMPLATE = "%s-%s-%s";

  public static final String id(final String symbol, final int year, final TimeFrame timeFrame) {
    return String.format(ID_TEMPLATE, symbol, year, timeFrame);
  }

  @Id
  private String id;

  @Index
  private String symbol;

  @Index
  private int year;

  @Index
  private TimeFrame timeFrame;

  @Unindex
  private List<DatastorePrice> data;

  private PriceDocument() {}

  public PriceDocument(final String symbol, final int year, final TimeFrame timeFrame) {
    this.id = id(symbol, year, timeFrame);
    this.symbol = symbol;
    this.year = year;
    this.timeFrame = timeFrame;
  }

  public String getId() {
    return id;
  }

  public List<Price> getData() {
    if (data == null) {
      data = Lists.newArrayList();
    }
    return data.stream()
        .map(d -> d.toPrice())
        .collect(Collectors.toList());
  }

  public void setData(final List<Price> prices) {
    this.data = prices.stream()
        .map(price -> DatastorePrice.copyFrom(price))
        .sorted(Comparator.comparing(price -> price.getDate()))
        .collect(Collectors.toList());
  }

  public String getSymbol() {
    return symbol;
  }

  public int getYear() {
    return year;
  }

  public TimeFrame getTimeFrame() {
    return timeFrame;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PriceDocument that = (PriceDocument) o;
    return year == that.year &&
        Objects.equal(id, that.id) &&
        Objects.equal(symbol, that.symbol) &&
        timeFrame == that.timeFrame &&
        Objects.equal(data, that.data);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, symbol, year, timeFrame, data);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("symbol", symbol)
        .add("year", year)
        .add("timeFrame", timeFrame)
        .add("data", data)
        .toString();
  }
}

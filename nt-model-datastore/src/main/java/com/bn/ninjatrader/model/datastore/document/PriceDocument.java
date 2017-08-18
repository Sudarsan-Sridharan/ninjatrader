package com.bn.ninjatrader.model.datastore.document;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.common.model.Price;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;

import java.util.List;

/**
 * Created by Brad on 6/3/16.
 */
@Entity
public class PriceDocument {

  public static final String id(final String symbol, final int year, final TimeFrame timeFrame) {
    return String.format("%s-%s-%s", symbol, year, timeFrame);
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
  private List<Price> data; // TODO transform to DatastorePrice w/ shorter attribute names

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
    return data;
  }

  public void setData(final List<Price> data) {
    this.data = data;
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
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final PriceDocument that = (PriceDocument) o;
    return Objects.equal(id, that.id) &&
        Objects.equal(data, that.data);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, data);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("data", data)
        .toString();
  }
}

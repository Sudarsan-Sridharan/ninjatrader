package com.bn.ninjatrader.model.datastore.entity;

import com.bn.ninjatrader.common.model.Price;
import com.bn.ninjatrader.model.datastore.translator.LocalDateTranslatorFactory;
import com.googlecode.objectify.annotation.Translate;

import java.time.LocalDate;

/**
 * @author bradwee2000@gmail.com
 */
public class DatastorePrice {

  public static final DatastorePrice copyFrom(final Price price) {
    return new DatastorePrice(price);
  }

  private double o; // open. Names are abbreviated to keep space usage low in datastore.

  private double h; // high

  private double l; // low

  private double c; // close

  private long v; // volume

  @Translate(value = LocalDateTranslatorFactory.class)
  private LocalDate d; // date

  /**
   * Objectify requires private no-arg constructor.
   */
  private DatastorePrice() {}

  public DatastorePrice(Price price) {
    this.d = price.getDate();
    this.o = price.getOpen();
    this.h = price.getHigh();
    this.l = price.getLow();
    this.c = price.getClose();
    this.v = price.getVolume();
  }

  public double getOpen() {
    return o;
  }

  public double getHigh() {
    return h;
  }

  public double getLow() {
    return l;
  }

  public double getClose() {
    return c;
  }

  public long getVolume() {
    return v;
  }

  public LocalDate getDate() {
    return d;
  }

  public Price toPrice() {
    return new Price(d, o, h, l, c, 0, v);
  }
}

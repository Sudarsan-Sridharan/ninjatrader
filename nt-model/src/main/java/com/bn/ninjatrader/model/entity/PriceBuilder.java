package com.bn.ninjatrader.model.entity;

import java.time.LocalDate;

/**
 * @author bradwee2000@gmail.com
 */
public interface PriceBuilder {

  PriceBuilder copyOf(final Price price);
  
  PriceBuilder date(final LocalDate date);

  PriceBuilder open(final double open);

  PriceBuilder high(final double high);

  PriceBuilder low(final double low);

  PriceBuilder close(final double close);

  PriceBuilder change(final double change);

  PriceBuilder volume(final long volume);

  PriceBuilder addVolume(final long volume);

  LocalDate getDate();

  double getOpen();

  double getHigh();

  double getLow();

  double getClose();

  double getChange();

  long getVolume();

  Price build();
}

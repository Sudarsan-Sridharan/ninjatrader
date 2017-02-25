package com.bn.ninjatrader.model.entity;

import java.time.LocalDate;

/**
 * @author bradwee2000@gmail.com
 */
public interface Price extends DateObj<Price> {

  LocalDate getDate();

  double getOpen();

  double getHigh();

  double getLow();

  double getClose();
  
  long getVolume();

  double getChange();
}

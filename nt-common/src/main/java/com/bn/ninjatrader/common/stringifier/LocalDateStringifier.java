package com.bn.ninjatrader.common.stringifier;

import com.googlecode.objectify.stringifier.Stringifier;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author bradwee2000@gmail.com
 */
public class LocalDateStringifier implements Stringifier<LocalDate> {
  @Override
  public String toString(final LocalDate localDate) {
    return localDate.format(DateTimeFormatter.BASIC_ISO_DATE);
  }

  @Override
  public LocalDate fromString(final String s) {
    return LocalDate.parse(s, DateTimeFormatter.BASIC_ISO_DATE);
  }
}

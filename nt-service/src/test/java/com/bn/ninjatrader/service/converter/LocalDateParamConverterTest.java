package com.bn.ninjatrader.service.converter;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author bradwee2000@gmail.com
 */
public class LocalDateParamConverterTest {

  private LocalDateParamConverter converter;

  @Before
  public void before() {
    converter = new LocalDateParamConverter();
  }

  @Test
  public void parseNullDate_shouldReturnNull() {
    assertThat(converter.fromString("")).isNull();
    assertThat(converter.fromString(null)).isNull();
  }

  @Test
  public void parseValidFormat_shouldReturnValidDates() {
    assertThat(converter.fromString("20160101")).isEqualTo(LocalDate.of(2016, 1, 1));
    assertThat(converter.fromString("19991231")).isEqualTo(LocalDate.of(1999, 12, 31));
  }

  @Test
  public void parseInvalidFormat_shouldThrowException() {
    assertThatExceptionOfType(DateTimeParseException.class).isThrownBy(() -> {
      assertThat(converter.fromString("2016101"));
    });
  }

  @Test
  public void parseInvalidDayOfMonth_shouldThrowException() {
    assertThatExceptionOfType(DateTimeParseException.class).isThrownBy(() -> {
      assertThat(converter.fromString("20161232"));
    });
  }

  @Test
  public void formatDate_shouldReturnValidStringFormat() {
    assertThat(converter.toString(LocalDate.of(2016, 2, 5))).isEqualTo("20160205");
    assertThat(converter.toString(LocalDate.of(2015, 12, 31))).isEqualTo("20151231");
  }
}

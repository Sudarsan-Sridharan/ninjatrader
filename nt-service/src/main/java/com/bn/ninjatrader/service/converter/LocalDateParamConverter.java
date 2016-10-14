package com.bn.ninjatrader.service.converter;

import com.bn.ninjatrader.common.util.DateFormats;
import com.bn.ninjatrader.service.provider.LocalDateParamConverterProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ext.ParamConverter;
import java.time.LocalDate;

/**
 * @author bradwee2000@gmail.com
 */
public class LocalDateParamConverter implements ParamConverter<LocalDate> {

  private static final Logger LOG = LoggerFactory.getLogger(LocalDateParamConverterProvider.class);

  @Override
  public LocalDate fromString(String value) {
    try {
      return LocalDate.parse(value, DateFormats.DB_DATE_FORMAT);
    } catch (Exception e) {
      LocalDateParamConverterProvider.LOG.error("Failed to parse {} to LocalDate.", value);
      throw e;
    }
  }

  @Override
  public String toString(LocalDate value) {
    return DateFormats.DB_DATE_FORMAT.format(value);
  }
}

package com.bn.ninjatrader.service.converter;

import com.bn.ninjatrader.common.util.DateFormats;
import com.bn.ninjatrader.service.provider.LocalDateParamConverterProvider;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ext.ParamConverter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class LocalDateParamConverter implements ParamConverter<LocalDate> {

  private static final Logger LOG = LoggerFactory.getLogger(LocalDateParamConverterProvider.class);

  @Override
  public LocalDate fromString(final String value) {
    if (StringUtils.isEmpty(value)) {
      return null;
    }

    try {
      return LocalDate.parse(value, DateTimeFormatter.BASIC_ISO_DATE);
    } catch (final Exception e) {
      LOG.error("Failed to parse {} to LocalDate.", value);
      throw e;
    }
  }

  @Override
  public String toString(LocalDate value) {
    return DateFormats.DB_DATE_FORMAT.format(value);
  }
}

package com.bn.ninjatrader.common.translator;

import com.googlecode.objectify.impl.Path;
import com.googlecode.objectify.impl.translate.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author bradwee2000@gmail.com
 */
public class LocalDateTranslatorFactory implements TranslatorFactory<LocalDate, String> {
  private static final Logger LOG = LoggerFactory.getLogger(LocalDateTranslatorFactory.class);

  @Override
  public Translator<LocalDate, String> create(final TypeKey<LocalDate> typeKey,
                                              final CreateContext createContext,
                                              final Path path) {
    return new LocalDateTranslator();
  }

  public static final class LocalDateTranslator implements Translator<LocalDate, String> {

    @Override
    public LocalDate load(final String s,
                          final LoadContext loadContext,
                          final Path path) throws SkipException {
      return LocalDate.parse(s, DateTimeFormatter.BASIC_ISO_DATE);
    }

    @Override
    public String save(final LocalDate localDate,
                       final boolean b,
                       final SaveContext saveContext,
                       final Path path) throws SkipException {
      return localDate.format(DateTimeFormatter.BASIC_ISO_DATE);
    }
  }
}

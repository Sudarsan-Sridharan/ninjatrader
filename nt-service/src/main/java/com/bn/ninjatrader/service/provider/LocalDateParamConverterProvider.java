package com.bn.ninjatrader.service.provider;

import com.bn.ninjatrader.service.converter.LocalDateParamConverter;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
@Provider
public class LocalDateParamConverterProvider implements ParamConverterProvider {
  private static final Logger LOG = LoggerFactory.getLogger(LocalDateParamConverterProvider.class);

  private final ParamConverter localDateParamConverter = new LocalDateParamConverter();

  @Override
  public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
    if (rawType != LocalDate.class) {
      return null;
    }
    return localDateParamConverter;
  }
}

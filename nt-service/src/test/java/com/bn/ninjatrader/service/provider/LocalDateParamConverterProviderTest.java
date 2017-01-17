package com.bn.ninjatrader.service.provider;

import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.ext.ParamConverter;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class LocalDateParamConverterProviderTest {

  private LocalDateParamConverterProvider provider;

  @Before
  public void before() {
    provider = new LocalDateParamConverterProvider();
  }

  @Test
  public void testGetConverterWithCorrectClass() {
    ParamConverter converter = provider.getConverter(LocalDate.class, null, null);
    assertThat(converter).isNotNull();
  }

  @Test
  public void testGetConverterWithWrongClass() {
    ParamConverter converter = provider.getConverter(String.class, null, null);
    assertThat(converter).isNull();

    converter = provider.getConverter(null, null, null);
    assertThat(converter).isNull();
  }
}

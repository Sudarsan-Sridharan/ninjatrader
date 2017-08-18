package com.bn.ninjatrader.service.client.guice;

import com.auth0.jwt.algorithms.Algorithm;
import com.bn.ninjatrader.service.client.service.ImportQuotesClientTest;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.netflix.archaius.api.Config;
import com.netflix.archaius.api.Property;
import com.netflix.archaius.api.PropertyContainer;
import com.netflix.archaius.api.PropertyFactory;
import org.junit.Before;
import org.junit.Test;

import java.time.Clock;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class NtServiceClientModuleTest {

  private PropertyFactory propertyFactory;
  private PropertyContainer propertyContainer;
  private Property property;

  @Before
  public void before() {
    propertyFactory = mock(PropertyFactory.class);
    propertyContainer = mock(PropertyContainer.class);
    property = mock(Property.class);

    when(propertyFactory.getProperty(anyString())).thenReturn(propertyContainer);
    when(propertyContainer.asString(anyString())).thenReturn(property);
  }

  @Test
  public void testCreate_shouldNotThrowExceptions() {
    final Injector injector = Guice.createInjector(
        new AbstractModule() {
          @Override
          protected void configure() {
            bind(PropertyFactory.class).toInstance(propertyFactory);
            bind(Algorithm.class).toInstance(mock(Algorithm.class));
            bind(Config.class).toInstance(mock(Config.class));
            bind(Clock.class).toInstance(Clock.systemDefaultZone());
          }
        },
        new NtServiceClientModule());

    injector.getInstance(ImportQuotesClientTest.class);
  }
}

package com.bn.ninjatrader.service.client.guice;

import com.bn.ninjatrader.service.client.annotation.ApiSecretKeyProperty;
import com.bn.ninjatrader.service.client.annotation.ServiceUrlProperty;
import com.bn.ninjatrader.service.client.filter.SecureClientRequestFilter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.netflix.archaius.api.Property;
import com.netflix.archaius.api.PropertyFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * @author bradwee2000@gmail.com
 */
public class NtServiceClientModule extends AbstractModule {

  @Override
  protected void configure() {
  }

  @Provides
  @ServiceUrlProperty
  private Property<String> serviceHost(final PropertyFactory propertyFactory) {
    return propertyFactory.getProperty("service.url").asString("");
  }

  @Provides
  @ApiSecretKeyProperty
  private Property<String> provideApiSecretKey(final PropertyFactory propertyFactory) {
    return propertyFactory.getProperty("api.secret.key").asString("test_key"); //TODO fix secret key
  }

  @Provides
  public Client provideClient(final SecureClientRequestFilter secureClientRequestFilter) {
    return ClientBuilder.newClient()
        .register(new JavaTimeModule())
        .register(secureClientRequestFilter);
  }
}

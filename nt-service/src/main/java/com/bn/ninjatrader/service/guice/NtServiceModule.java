package com.bn.ninjatrader.service.guice;

import com.google.inject.AbstractModule;

import java.time.Clock;

/**
 * @author bradwee2000@gmail.com
 */
public class NtServiceModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(Clock.class).toInstance(Clock.systemDefaultZone());
  }
}

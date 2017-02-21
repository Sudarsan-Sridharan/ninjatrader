package com.bn.ninjatrader.common.guice;

import com.bn.ninjatrader.common.util.DateUtil;
import com.google.inject.PrivateModule;

import java.time.Clock;

/**
 * @author bradwee2000@gmail.com
 */
public class NtClockModule extends PrivateModule {
  @Override
  protected void configure() {
    bind(Clock.class).toInstance(DateUtil.phClock());
    expose(Clock.class);
  }
}

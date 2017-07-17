package com.bn.ninjatrader.event.guice;

import com.bn.ninjatrader.event.broker.DefaultMessageBroker;
import com.bn.ninjatrader.event.broker.MessageBroker;
import com.google.inject.AbstractModule;

/**
 * @author bradwee2000@gmail.com
 */
public class NtEventModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(MessageBroker.class).to(DefaultMessageBroker.class);
  }
}

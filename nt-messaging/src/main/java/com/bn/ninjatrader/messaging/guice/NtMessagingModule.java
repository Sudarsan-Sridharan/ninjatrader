package com.bn.ninjatrader.messaging.guice;

import com.bn.ninjatrader.messaging.MessagingClient;
import com.bn.ninjatrader.messaging.hazelcast.HazelcastMessagingClient;
import com.google.inject.AbstractModule;

/**
 * @author bradwee2000@gmail.com
 */
public class NtMessagingModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(MessagingClient.class).to(HazelcastMessagingClient.class);
  }
}

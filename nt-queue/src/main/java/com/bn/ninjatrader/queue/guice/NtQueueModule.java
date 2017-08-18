package com.bn.ninjatrader.queue.guice;

import com.bn.ninjatrader.queue.QueueClient;
import com.bn.ninjatrader.queue.hazelcast.HazelcastQueueClient;
import com.google.inject.AbstractModule;

/**
 * @author bradwee2000@gmail.com
 */
public class NtQueueModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(QueueClient.class).to(HazelcastQueueClient.class);
  }
}

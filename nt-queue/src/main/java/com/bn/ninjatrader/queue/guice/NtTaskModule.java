package com.bn.ninjatrader.queue.guice;

import com.bn.ninjatrader.queue.TaskDispatcher;
import com.bn.ninjatrader.queue.hazelcast.HazelcastTaskDispatcher;
import com.google.inject.AbstractModule;

/**
 * @author bradwee2000@gmail.com
 */
public class NtTaskModule extends AbstractModule {
  @Override
  protected void configure() {
    bindTaskDispatcher();
  }

  protected void bindTaskDispatcher() {
    bind(TaskDispatcher.class).to(HazelcastTaskDispatcher.class);
  }
}

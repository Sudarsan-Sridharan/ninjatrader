package com.bn.ninjatrader.service.guice;

import com.bn.ninjatrader.auth.guice.NtAuthModule;
import com.bn.ninjatrader.messaging.guice.NtMessagingModule;
import com.bn.ninjatrader.model.mongo.guice.NtModelMongoModule;
import com.bn.ninjatrader.queue.guice.NtQueueModule;
import com.bn.ninjatrader.simulation.guice.NtSimulationModule;
import com.google.inject.AbstractModule;
import com.netflix.archaius.guice.ArchaiusModule;

/**
 * @author bradwee2000@gmail.com
 */
public class NtServiceModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new ArchaiusModule());
    install(new NtAuthModule());
    install(new NtQueueModule());
    install(new NtMessagingModule());
    install(new NtServiceCacheModule());
    install(new NtServiceEventModule());
    install(new NtServiceSimulationModule());
    install(new NtModelMongoModule());
  }
}

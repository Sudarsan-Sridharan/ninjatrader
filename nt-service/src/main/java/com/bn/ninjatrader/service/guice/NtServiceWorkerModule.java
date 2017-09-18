package com.bn.ninjatrader.service.guice;

import com.bn.ninjatrader.worker.HazelcastWorkerDispatcher;
import com.bn.ninjatrader.worker.WorkerDispatcher;
import com.google.inject.AbstractModule;

/**
 * @author bradwee2000@gmail.com
 */
public class NtServiceWorkerModule extends AbstractModule {

  @Override
  protected void configure() {
    bindWorkerDispatcher();
  }
  protected void bindWorkerDispatcher() {
    bind(WorkerDispatcher.class).to(HazelcastWorkerDispatcher.class);
  }
}

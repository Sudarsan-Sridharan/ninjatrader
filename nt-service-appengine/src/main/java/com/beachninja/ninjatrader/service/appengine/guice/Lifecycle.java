package com.beachninja.ninjatrader.service.appengine.guice;

import com.bn.ninjatrader.worker.WorkerDispatcher;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class Lifecycle {

  private final WorkerDispatcher workerDispatcher;

  @Inject
  public Lifecycle(final WorkerDispatcher workerDispatcher) {
    this.workerDispatcher = workerDispatcher;
  }

  public void start() {
    workerDispatcher.connect("");
  }

  public void stop() {
    workerDispatcher.shutdown();
  }
}


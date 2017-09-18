package com.bn.ninjatrader.service.dropwizard.lifecycle;

import com.bn.ninjatrader.worker.WorkerDispatcher;
import io.dropwizard.lifecycle.Managed;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class ManagedWorkerClient implements Managed {

  private final WorkerDispatcher workerClient;
  private final String host;

  @Inject
  public ManagedWorkerClient(final WorkerDispatcher workerClient, final String host) {
    this.workerClient = workerClient;
    this.host = host;
  }

  @Override
  public void start() throws Exception {
    workerClient.connect(host);
  }

  @Override
  public void stop() throws Exception {
    workerClient.shutdown();
  }
}

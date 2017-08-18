package com.bn.ninjatrader.service.dropwizard.lifecycle;

import com.bn.ninjatrader.queue.QueueClient;
import io.dropwizard.lifecycle.Managed;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class ManagedQueueClient implements Managed {

  private final QueueClient queueClient;
  private final String host;

  @Inject
  public ManagedQueueClient(final QueueClient queueClient, final String host) {
    this.queueClient = queueClient;
    this.host = host;
  }

  @Override
  public void start() throws Exception {
    queueClient.connect(host);
  }

  @Override
  public void stop() throws Exception {
    queueClient.shutdown();
  }
}

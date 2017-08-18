package com.bn.ninjatrader.service.dropwizard.lifecycle;

import com.bn.ninjatrader.messaging.MessagingClient;
import io.dropwizard.lifecycle.Managed;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class ManagedMessagingClient implements Managed {

  private final MessagingClient messagingClient;
  private final String host;

  @Inject
  public ManagedMessagingClient(final MessagingClient messagingClient, final String host) {
    this.messagingClient = messagingClient;
    this.host = host;
  }

  @Override
  public void start() throws Exception {
    messagingClient.connect(host);
  }

  @Override
  public void stop() throws Exception {
    messagingClient.shutdown();
  }
}

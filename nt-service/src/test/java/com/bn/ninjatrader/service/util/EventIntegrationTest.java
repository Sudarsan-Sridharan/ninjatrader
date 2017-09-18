package com.bn.ninjatrader.service.util;

import com.bn.ninjatrader.event.dispatcher.DefaultMessagePublisher;
import com.bn.ninjatrader.event.dispatcher.MessagePublisher;
import com.bn.ninjatrader.event.guice.provider.EventTopicsProvider;
import com.bn.ninjatrader.messaging.hazelcast.HazelcastMessagingClient;
import com.bn.ninjatrader.messaging.listener.MessageListener;
import com.bn.ninjatrader.service.filter.EventDispatchFilter;
import com.google.common.collect.Multimap;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bradwee2000@gmail.com
 */
public abstract class EventIntegrationTest extends JerseyTest {
  private static final Logger LOG = LoggerFactory.getLogger(EventIntegrationTest.class);
  private static HazelcastMessagingClient messagingClient;

  /**
   * Need to call this on beforeClass so resources only startup once and
   * event listeners don't get added multiple times.
   */
  protected static ResourceConfig integrateApplication(final Multimap<String, MessageListener> subscribers) {
    messagingClient = new HazelcastMessagingClient();
    messagingClient.connectLocal();

    final EventTopicsProvider eventTopicsProvider = new EventTopicsProvider(messagingClient, subscribers);
    final MessagePublisher messageDispatcher = new DefaultMessagePublisher(eventTopicsProvider);
    final EventDispatchFilter eventDispatchFilter = new EventDispatchFilter(messageDispatcher);
    return new ResourceConfig().register(eventDispatchFilter);
  }

  protected static void shutDown() {
    if (messagingClient != null) {
      messagingClient.shutdown();
    }
  }
}

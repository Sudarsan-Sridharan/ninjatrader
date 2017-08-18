package com.bn.ninjatrader.event.guice.provider;

import com.bn.ninjatrader.event.annotation.Subscribers;
import com.bn.ninjatrader.messaging.MessagingClient;
import com.bn.ninjatrader.messaging.listener.MessageListener;
import com.bn.ninjatrader.messaging.topic.Topic;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Map;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class EventTopicsProvider implements Provider<Map<String, Topic>> {
  private static final Logger LOG = LoggerFactory.getLogger(EventTopicsProvider.class);

  private final Multimap<String, MessageListener> subscribers;
  private final MessagingClient messagingClient;

  @Inject
  public EventTopicsProvider(final MessagingClient messagingClient,
                             @Subscribers final Multimap<String, MessageListener> subscribers) {
    this.messagingClient = messagingClient;
    this.subscribers = subscribers;
  }

  @Override
  public Map<String, Topic> get() {
    final ImmutableMap.Builder<String, Topic> map = ImmutableMap.builder();

    subscribers.keySet().forEach(eventKey -> {
      final Topic topic = messagingClient.getTopic(eventKey);

      LOG.info("Created topic for event: {}", eventKey);

      map.put(eventKey, topic);

      final Collection<MessageListener> handlers = subscribers.get(eventKey);
      handlers.forEach(handler -> {
        messagingClient.addSubscriber(eventKey, handler);
      });
    });
    return map.build();
  }
}

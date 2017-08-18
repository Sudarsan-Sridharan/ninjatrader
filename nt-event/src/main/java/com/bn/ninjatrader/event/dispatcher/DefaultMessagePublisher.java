package com.bn.ninjatrader.event.dispatcher;

import com.bn.ninjatrader.event.message.Message;
import com.bn.ninjatrader.event.annotation.EventTopics;
import com.bn.ninjatrader.messaging.topic.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class DefaultMessagePublisher implements MessagePublisher {
  private static final Logger LOG = LoggerFactory.getLogger(DefaultMessagePublisher.class);

  private Provider<Map<String, Topic>> topicsProvider;
  private Map<String, Topic> topics;

  @Inject
  public DefaultMessagePublisher(@EventTopics final Provider<Map<String, Topic>> topicsProvider) {
    this.topicsProvider = topicsProvider;
  }

  @Override
  public void publish(final Message message) {
    checkConditions(message);

    final String eventKey = message.getEventKey();
    checkState(topics.containsKey(eventKey), "Topic not found for event: " + eventKey);

    topics.get(eventKey).publish(message);
  }

  private void checkConditions(final Message message) {
    checkNotNull(message, "Message must not be null.");
    checkNotNull(message.getEventKey(), "Message event key must not be null.");
    if (topics == null) {
      topics = topicsProvider.get();
    }
  }
}

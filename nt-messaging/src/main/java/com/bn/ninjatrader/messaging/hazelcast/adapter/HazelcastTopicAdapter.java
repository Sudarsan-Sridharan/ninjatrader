package com.bn.ninjatrader.messaging.hazelcast.adapter;

import com.bn.ninjatrader.messaging.topic.Topic;
import com.google.common.base.Preconditions;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.MessageListener;

/**
 * @author bradwee2000@gmail.com
 */
public class HazelcastTopicAdapter<T> implements Topic<T> {

  private final ITopic<T> topic;

  public HazelcastTopicAdapter(final ITopic<T> topic) {
    Preconditions.checkNotNull(topic, "ITopic must not be null.");
    this.topic = topic;
  }

  @Override
  public String getName() {
    return topic.getName();
  }

  @Override
  public void publish(T message) {
    topic.publish(message);
  }

  @Override
  public String addListener(MessageListener<T> listener) {
    return topic.addMessageListener(listener);
  }

  @Override
  public boolean removeMessageListener(String listenerID) {
    return topic.removeMessageListener(listenerID);
  }

  @Override
  public void destroy() {
    topic.destroy();
  }
}

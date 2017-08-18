package com.bn.ninjatrader.messaging.topic;

import com.hazelcast.core.MessageListener;

/**
 * @author bradwee2000@gmail.com
 */
public interface Topic<T> {

  /**
   * Gets topic name
   * @return Name of topic
   */
  String getName();

  /**
   * Publishes message to all subscribed listeners
   * @param message Message to publish
   */
  void publish(T message);

  /**
   * Add listener to topic so any message sent to topic will be sent to this listener
   * @return listener ID
   */
  String addListener(MessageListener<T> listener);

  /**
   * Remove listener from topic
   * @param listenerID
   * @return true if listener was removed.
   */
  boolean removeMessageListener(String listenerID);

  /**
   * Destroy topic.
   */
  void destroy();
}

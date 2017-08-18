package com.bn.ninjatrader.messaging;

import com.bn.ninjatrader.messaging.listener.MessageListener;
import com.bn.ninjatrader.messaging.topic.Topic;

import java.util.Collection;

/**
 * @author bradwee2000@gmail.com
 */
public interface MessagingClient {

  /**
   * Start and connect to local messaging server.
   */
  void connectLocal();

  /**
   * Connect to messaging server using the address.
   * @param address Address of the messaging server.
   */
  void connect(final String address);

  /**
   * Connect to messaging server using the addresses.
   * @param addresses Addresses of the messaging servers.
   */
  void connect(final Collection<String> addresses);

  /**
   * Shutdown the messaging client.
   */
  void shutdown();

  /**
   * Gets topic from messaging server.
   * @param topicName Name of topic
   */
  <T> Topic<T> getTopic(final String topicName);

  /**
   * Add message listener to the topic.
   * @param topicName Name of Topic
   * @param messageListener Subscriber
   */
  void addSubscriber(String topicName, MessageListener messageListener);
}

package com.bn.ninjatrader.queue;

import com.bn.ninjatrader.queue.listener.QueueItemListener;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;

/**
 * @author bradwee2000@gmail.com
 */
public interface QueueClient {

  <T> BlockingQueue<T> getQueue(final String queueName);

  QueueClient addListsner(final String queueName, final QueueItemListener listener);

  /**
   * Connect to cache server using the address.
   * @param address Address of the queue server.
   */
  void connect(final String address);

  /**
   * Connect to cache server using the addresses.
   * @param addresses Addresses of the queue servers.
   */
  void connect(final Collection<String> addresses);

  /**
   * Shutdown the queue client.
   */
  void shutdown();
}

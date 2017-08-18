package com.bn.ninjatrader.event.dispatcher;

import com.bn.ninjatrader.event.message.Message;

/**
 * Handles dispatching messages to subscribers.
 *
 * @author bradwee2000@gmail.com
 */
public interface MessagePublisher {

  /**
   * Sends the message to all subscribers.
   * @param message Message to send.
   */
  void publish(final Message message);
}

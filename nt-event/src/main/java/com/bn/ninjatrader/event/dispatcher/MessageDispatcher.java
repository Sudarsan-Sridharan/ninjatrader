package com.bn.ninjatrader.event.dispatcher;

import com.bn.ninjatrader.event.Message;

/**
 * Handles dispatching messages to subscribers.
 *
 * @author bradwee2000@gmail.com
 */
public interface MessageDispatcher {

  /**
   * Sends the message to all subscribers.
   * @param message Message to send.
   */
  void dispatch(final Message message);
}

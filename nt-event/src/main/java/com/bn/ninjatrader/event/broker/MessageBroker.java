package com.bn.ninjatrader.event.broker;

import com.bn.ninjatrader.event.Message;
import com.bn.ninjatrader.event.handler.MessageHandler;
import com.bn.ninjatrader.event.type.EventType;

/**
 * Handles subscription to events and dispatching messages.
 *
 * @author bradwee2000@gmail.com
 */
public interface MessageBroker {

  /**
   * Subscribe to and event.
   * @param eventType EventType to subscribe to.
   * @param messageHandler Handles the message when event is triggered.
   */
  void subscribe(final EventType eventType, final MessageHandler messageHandler);

  /**
   * Sends the message to all subscribers.
   * @param message Message to send.
   */
  void dispatch(final Message message);
}

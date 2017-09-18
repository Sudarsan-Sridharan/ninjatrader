package com.bn.ninjatrader.messaging.listener;

import com.bn.ninjatrader.messaging.Message;

import java.time.LocalDateTime;

/**
 * @author bradwee2000@gmail.com
 */
public interface MessageListener<T> {

  /**
   * Called when topic publishes a message.
   * @param message Message object
   * @param publishTime time message was published
   */
  void onMessage(Message<T> message, LocalDateTime publishTime);
}

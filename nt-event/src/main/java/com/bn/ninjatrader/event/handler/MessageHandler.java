package com.bn.ninjatrader.event.handler;

import com.bn.ninjatrader.event.Message;

/**
 * @author bradwee2000@gmail.com
 */
public interface MessageHandler {

  void handle(Message message);
}

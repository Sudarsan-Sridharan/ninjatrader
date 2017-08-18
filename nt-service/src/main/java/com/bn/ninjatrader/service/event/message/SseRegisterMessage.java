package com.bn.ninjatrader.service.event.message;

import com.bn.ninjatrader.event.message.Message;
import com.bn.ninjatrader.service.event.EventTypes;

/**
 * @author bradwee2000@gmail.com
 */
public class SseRegisterMessage extends Message<String> {

  public SseRegisterMessage(final String userId) {
    super(EventTypes.SSE_REGISTER, userId);
  }
}

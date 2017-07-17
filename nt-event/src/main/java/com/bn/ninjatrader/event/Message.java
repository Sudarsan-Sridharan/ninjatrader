package com.bn.ninjatrader.event;

import com.bn.ninjatrader.event.type.EventType;

/**
 * @author bradwee2000@gmail.com
 */
public interface Message {

  EventType getEventType();
}

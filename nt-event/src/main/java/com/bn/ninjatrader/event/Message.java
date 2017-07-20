package com.bn.ninjatrader.event;

import com.google.common.base.MoreObjects;

/**
 * @author bradwee2000@gmail.com
 */
public abstract class Message<T> {

  private final String eventKey;
  private final T payload;

  public Message(String eventKey, T payload) {
    this.eventKey = eventKey;
    this.payload = payload;
  }

  public String getEventKey() {
    return eventKey;
  }

  public T getPayload() {
    return payload;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("eventKey", eventKey)
        .add("payload", payload)
        .toString();
  }
}

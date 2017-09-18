package com.bn.ninjatrader.messaging;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.MoreObjects;

import java.io.Serializable;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public class Message<T> implements Serializable {

  @JsonProperty("messageType")
  private final String messageType;

  @JsonProperty("payload")
  private final T payload;

  public Message(@JsonProperty("messageType") final String messageType,
                 @JsonProperty("payload") T payload) {
    this.messageType = messageType;
    this.payload = payload;
  }

  public String getMessageType() {
    return messageType;
  }

  public T getPayload() {
    return payload;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("messageType", messageType)
        .add("payload", payload)
        .toString();
  }
}

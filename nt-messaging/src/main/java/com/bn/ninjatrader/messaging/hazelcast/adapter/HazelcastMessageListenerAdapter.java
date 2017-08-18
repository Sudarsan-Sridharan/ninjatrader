package com.bn.ninjatrader.messaging.hazelcast.adapter;

import com.bn.ninjatrader.messaging.listener.MessageListener;
import com.hazelcast.core.Message;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author bradwee2000@gmail.com
 */
public class HazelcastMessageListenerAdapter<T> implements com.hazelcast.core.MessageListener<T> {

  final MessageListener<T> listener;

  public HazelcastMessageListenerAdapter(final MessageListener<T> listener) {
    this.listener = listener;
  }

  @Override
  public void onMessage(Message<T> message) {
    final LocalDateTime publishTime = Instant.ofEpochMilli(message.getPublishTime())
        .atOffset(ZoneOffset.UTC).toLocalDateTime();
    this.listener.onMessage(message.getMessageObject(), publishTime);
  }
}

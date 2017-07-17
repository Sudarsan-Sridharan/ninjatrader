package com.bn.ninjatrader.event.broker;

import com.bn.ninjatrader.event.Message;
import com.bn.ninjatrader.event.handler.MessageHandler;
import com.bn.ninjatrader.event.type.EventType;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import javax.inject.Singleton;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class DefaultMessageBroker implements MessageBroker {

  private Multimap<EventType, MessageHandler> subscribers = ArrayListMultimap.create();

  @Override
  public void subscribe(final EventType eventType, final MessageHandler messageHandler) {
    subscribers.put(eventType, messageHandler);
  }

  @Override
  public void dispatch(final Message message) {
    checkNotNull(message, "Message must not be null.");
    checkNotNull(message.getEventType(), "Message EventType must not be null.");

    final Collection<MessageHandler> handlers = subscribers.get(message.getEventType());
    if (handlers == null) {
      return;
    }

    handlers.forEach(handler -> {
        handler.handle(message);
    });
  }
}

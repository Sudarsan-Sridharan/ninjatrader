package com.bn.ninjatrader.event.dispatcher;

import com.bn.ninjatrader.event.Message;
import com.bn.ninjatrader.event.annotation.Subscribers;
import com.bn.ninjatrader.event.handler.MessageHandler;
import com.google.common.collect.Multimap;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class DefaultMessageDispatcher implements MessageDispatcher {

  private final Multimap<String, MessageHandler> subscribers;

  @Inject
  public DefaultMessageDispatcher(@Subscribers final Multimap<String, MessageHandler> subscribers) {
    this.subscribers = subscribers;
  }

  @Override
  public void dispatch(final Message message) {
    checkNotNull(message, "Message must not be null.");
    checkNotNull(message.getEventKey(), "Message event key must not be null.");

    final Collection<MessageHandler> handlers = subscribers.get(message.getEventKey());
    if (handlers == null) {
      return;
    }

    handlers.forEach(handler -> {
        handler.handle(message);
    });
  }
}

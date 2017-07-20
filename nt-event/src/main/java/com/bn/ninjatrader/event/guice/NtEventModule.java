package com.bn.ninjatrader.event.guice;

import com.bn.ninjatrader.event.annotation.Subscribers;
import com.bn.ninjatrader.event.dispatcher.DefaultMessageDispatcher;
import com.bn.ninjatrader.event.dispatcher.MessageDispatcher;
import com.bn.ninjatrader.event.handler.MessageHandler;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.AbstractModule;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
public class NtEventModule extends AbstractModule {

  private Multimap<String, MessageHandler> subscribers = ArrayListMultimap.create();

  @Override
  protected void configure() {
    bind(MessageDispatcher.class).to(DefaultMessageDispatcher.class);
    bind(Multimap.class).annotatedWith(Subscribers.class)
        .toInstance(ImmutableListMultimap.copyOf(subscribers));
  }

  public NtEventModule addSubscriber(final String eventType, final MessageHandler handler) {
    checkNotNull(eventType, "EventType must not be null.");
    checkNotNull(handler, "MessageHandler must not be null.");
    subscribers.put(eventType, handler);
    return this;
  }
}

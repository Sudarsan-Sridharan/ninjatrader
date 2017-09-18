package com.bn.ninjatrader.event.guice;

import com.bn.ninjatrader.event.annotation.EventTopics;
import com.bn.ninjatrader.event.dispatcher.DefaultMessagePublisher;
import com.bn.ninjatrader.event.dispatcher.MessagePublisher;
import com.bn.ninjatrader.event.guice.provider.EventTopicsProvider;
import com.bn.ninjatrader.messaging.topic.Topic;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

import javax.inject.Singleton;
import java.util.Map;

/**
 * Requires binding to type Multimap annotated with Subscribers.
 *
 * @author bradwee2000@gmail.com
 */
public class NtEventModule extends AbstractModule {

  @Override
  protected void configure() {
    bindMessagePublisher();

    // Bind Topics map to a provider
    bind(new TypeLiteral<Map<String, Topic>>() {}).annotatedWith(EventTopics.class)
        .toProvider(EventTopicsProvider.class).in(Singleton.class);
  }

  protected void bindMessagePublisher() {
    bind(MessagePublisher.class).to(DefaultMessagePublisher.class);
  }
}

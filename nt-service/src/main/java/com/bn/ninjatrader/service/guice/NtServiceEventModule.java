package com.bn.ninjatrader.service.guice;

import com.bn.ninjatrader.event.annotation.Subscribers;
import com.bn.ninjatrader.event.guice.NtEventModule;
import com.bn.ninjatrader.messaging.listener.MessageListener;
import com.bn.ninjatrader.service.event.EventTypes;
import com.bn.ninjatrader.service.event.handler.ImportedFullPricesHandler;
import com.bn.ninjatrader.service.event.handler.SseRegisterHandler;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Singleton;

/**
 * @author bradwee2000@gmail.com
 */
public class NtServiceEventModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new NtEventModule());
  }

  @Singleton
  @Provides
  @Subscribers
  public Multimap<String, MessageListener> subscribers(final ImportedFullPricesHandler importedClosingPricesHandler,
                                                       final SseRegisterHandler sseRegisterHandler) {
    return ImmutableMultimap.<String, MessageListener>builder()
        .put(EventTypes.IMPORTED_FULL_PRICES, importedClosingPricesHandler)
        .put(EventTypes.SSE_REGISTER, sseRegisterHandler)
        .build();
  }
}

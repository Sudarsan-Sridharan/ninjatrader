package com.bn.ninjatrader.service.util;

import com.bn.ninjatrader.event.dispatcher.DefaultMessageDispatcher;
import com.bn.ninjatrader.event.dispatcher.MessageDispatcher;
import com.bn.ninjatrader.event.handler.MessageHandler;
import com.bn.ninjatrader.service.filter.EventDispatchFilter;
import com.google.common.collect.Multimap;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import javax.ws.rs.core.Application;

/**
 * @author bradwee2000@gmail.com
 */
public abstract class EventIntegrationTest extends JerseyTest {

  @Override
  protected Application configure() {
    final MessageDispatcher messageDispatcher = new DefaultMessageDispatcher(prepareSubscribers());
    final EventDispatchFilter eventDispatchFilter = new EventDispatchFilter(messageDispatcher);
    return new ResourceConfig().register(eventDispatchFilter);
  }

  public abstract Multimap<String, MessageHandler> prepareSubscribers();
}

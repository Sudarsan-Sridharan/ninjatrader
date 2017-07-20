package com.bn.ninjatrader.event.dispatcher;

import com.bn.ninjatrader.event.Message;
import com.bn.ninjatrader.event.handler.MessageHandler;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author bradwee2000@gmail.com
 */
public class DefaultMessageDispatcherTest {
  private Multimap<String, MessageHandler> subscribers = ArrayListMultimap.create();
  private MessageDispatcher broker;

  @Before
  public void before() {
    subscribers.clear();
    broker = new DefaultMessageDispatcher(subscribers);
  }

  @Test
  public void testSubscribeToEvent_shouldCallSubscriberWhenSameEventIsTriggered() {
    // Subscribe to event
    final MessageHandler handler1 = mock(MessageHandler.class);
    final MessageHandler handler2 = mock(MessageHandler.class);

    subscribers.put("Event1", handler1);
    subscribers.put("Event1", handler2);

    // Dispatch same event
    final Message message = new Message("Event1", null) {};
    broker.dispatch(message);

    // Should call subscribers
    verify(handler1).handle(message);
    verify(handler2).handle(message);
  }

  @Test
  public void testSubscribeToEvent_shouldNotCallSubscriberWhenDiffEventIsTriggered() {
    // Subscribe to event
    final MessageHandler handler = mock(MessageHandler.class);
    subscribers.put("Event1", handler);

    // Dispatch different event
    final Message message = new Message("AnotherEvent", null) {};
    broker.dispatch(message);

    // Should not call subscriber
    verify(handler, times(0)).handle(any());
  }
}

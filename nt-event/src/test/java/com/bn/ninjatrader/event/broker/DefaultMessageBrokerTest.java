package com.bn.ninjatrader.event.broker;

import com.bn.ninjatrader.event.Message;
import com.bn.ninjatrader.event.handler.MessageHandler;
import com.bn.ninjatrader.event.type.EventType;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author bradwee2000@gmail.com
 */
public class DefaultMessageBrokerTest {

  private MessageBroker broker;

  @Before
  public void before() {
    broker = new DefaultMessageBroker();
  }

  @Test
  public void testSubscribeToEvent_shouldCallSubscriberWhenSameEventIsTriggered() {
    // Subscribe to event
    final MessageHandler handler1 = mock(MessageHandler.class);
    final MessageHandler handler2 = mock(MessageHandler.class);
    broker.subscribe(EventType.IMPORTED_FULL_PRICES, handler1);
    broker.subscribe(EventType.IMPORTED_FULL_PRICES, handler2);

    // Dispatch same event
    final Message message = () -> EventType.IMPORTED_FULL_PRICES;
    broker.dispatch(message);

    // Should call subscribers
    verify(handler1).handle(message);
    verify(handler2).handle(message);
  }

  @Test
  public void testSubscribeToEvent_shouldNotCallSubscriberWhenDiffEventIsTriggered() {
    // Subscribe to event
    final MessageHandler handler = mock(MessageHandler.class);
    broker.subscribe(EventType.IMPORTED_FULL_PRICES, handler);

    // Dispatch different event
    final Message message = () -> EventType.IMPORTED_CLOSING_PRICES;
    broker.dispatch(message);

    // Should not call subscriber
    verify(handler, times(0)).handle(any());
  }
}

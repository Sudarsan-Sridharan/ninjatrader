package com.bn.ninjatrader.event.dispatcher;

import com.bn.ninjatrader.messaging.Message;
import com.bn.ninjatrader.messaging.topic.Topic;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Provider;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author bradwee2000@gmail.com
 */
public class DefaultMessagePublisherTest {
  private Provider<Map<String, Topic>> topicsProvider;
  private MessagePublisher messagePublisher;
  private Topic topic1;
  private Topic topic2;

  @Before
  public void before() {
    topic1 = mock(Topic.class);
    topic2 = mock(Topic.class);
    topicsProvider = () -> ImmutableMap.of("Event1", topic1, "Event2", topic2);
    messagePublisher = new DefaultMessagePublisher(topicsProvider);
  }

  @Test
  public void testPublishEvent_shouldPublishToTopic() {
    final Message message1 = new Message("Event1", null) {};
    final Message message2 = new Message("Event2", null) {};

    // Verify only topic1 gets the message
    messagePublisher.publish(message1);
    verify(topic1).publish(message1);
    verify(topic2, times(0)).publish(message1);

    // Verify only topic2 gets the message
    messagePublisher.publish(message2);
    verify(topic2).publish(message2);
    verify(topic1, times(0)).publish(message2);
  }
}

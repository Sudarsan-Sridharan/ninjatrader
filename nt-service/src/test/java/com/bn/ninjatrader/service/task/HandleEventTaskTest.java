package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.messaging.Message;
import com.bn.ninjatrader.messaging.listener.MessageListener;
import com.bn.ninjatrader.model.util.ObjectMapperProvider;
import com.bn.ninjatrader.service.provider.ObjectMapperContextResolver;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;

import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class HandleEventTaskTest extends JerseyTest {
  private static final Logger LOG = LoggerFactory.getLogger(HandleEventTaskTest.class);

  private DummyMessageListener messageListener;

  @Override
  protected Application configure() {
    messageListener = new DummyMessageListener();

    final Multimap<String, MessageListener> subscribers = ArrayListMultimap.create();
    subscribers.put("event1", messageListener);

    final ObjectMapperProvider omProvider = new ObjectMapperProvider();
    omProvider.get().registerSubtypes(DummyMessage.class);

    final ObjectMapperContextResolver omResolver = new ObjectMapperContextResolver(omProvider);

    return new ResourceConfig().register(new HandleEventTask(subscribers)).register(omResolver);
  }

  @Test
  public void testHandleEvent_shouldRunSubscribersForThatEvent() {
    final Response response = target("/tasks/handle-event")
        .request()
        .post(Entity.json(new Message("event1", "payload")));
    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
    assertThat(messageListener.getReceivedMessage().getMessageType()).isEqualTo("event1");
    assertThat(messageListener.getReceivedMessage().getPayload()).isEqualTo("payload");
  }

  @Test
  public void testHandleEvent_shouldNotRunSubscribersNotSubscribedToThatEvent() {
    final Response response = target("/tasks/handle-event")
        .request()
        .post(Entity.json(new Message("event2", "payload")));
    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

    assertThat(messageListener.getReceivedMessage()).isNull();
  }

  @Test
  public void testHandleSubclassEvent_should() {
    final Response response = target("/tasks/handle-event")
        .request()
        .post(Entity.json(new DummyMessage("Dummy Payload")));
    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
    assertThat(messageListener.getReceivedMessage().getMessageType()).isEqualTo("event1");
    assertThat(messageListener.getReceivedMessage().getPayload()).isEqualTo("Dummy Payload");
    assertThat(messageListener.getReceivedMessage()).isInstanceOf(DummyMessage.class);
  }

  /**
   * Dummy Messasge
   */
  public static final class DummyMessage extends Message<String> {

    // Needed for json
    public DummyMessage() {
      super("event1", null);
    }

    public DummyMessage(String payload) {
      super("event1", payload);
    }
  }

  /**
   * Dummy listener
   */
  public static final class DummyMessageListener implements MessageListener<Message> {
    private Message receivedMessage;

    @Override
    public void onMessage(Message message, LocalDateTime publishTime) {
      this.receivedMessage = message;
    }

    public Message getReceivedMessage() {
      return receivedMessage;
    }
  }
}

package com.bn.ninjatrader.event.dispatcher;

import com.bn.ninjatrader.messaging.Message;
import com.bn.ninjatrader.model.util.ObjectMapperProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.charset.Charset;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class AppEngineMessagePublisher implements MessagePublisher {
  private static final Logger LOG = LoggerFactory.getLogger(AppEngineMessagePublisher.class);
  private static final String QUEUE_NAME = "events";

  private final ObjectMapper om;

  @Inject
  public AppEngineMessagePublisher(final ObjectMapperProvider objectMapperProvider) {
    this.om = objectMapperProvider.get();
  }

  @Override
  public void publish(final Message message) {
    checkConditions(message);

    try {
      final TaskOptions taskOptions = TaskOptions.Builder.withUrl("/tasks/handle-event")
          .method(TaskOptions.Method.POST)
          .payload(om.writeValueAsString(message).getBytes(Charset.forName("UTF-8")), "application/json");

      QueueFactory.getQueue(QUEUE_NAME).addAsync(taskOptions);

    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private void checkConditions(final Message message) {
    checkNotNull(message, "Message must not be null.");
    checkNotNull(message.getMessageType(), "Message event key must not be null.");
  }
}

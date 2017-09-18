package com.bn.ninjatrader.queue.appengine;

import com.bn.ninjatrader.model.util.ObjectMapperProvider;
import com.bn.ninjatrader.queue.TaskDispatcher;
import com.bn.ninjatrader.queue.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import java.nio.charset.Charset;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class AppEngineTaskDispatcher implements TaskDispatcher { //TODO finish
  private static final String QUEUE_NAME = "tasks";
  private static final Charset UTF8 = Charset.forName("UTF-8");

  private final ObjectMapper om;

  @Inject
  public AppEngineTaskDispatcher(final ObjectMapperProvider objectMapperProvider) {
    this.om = objectMapperProvider.get();
  }

  @Override
  public void submitTask(final Task task) {
    try {
      final TaskOptions taskOptions = TaskOptions.Builder.withUrl(task.getPath())
          .method(TaskOptions.Method.POST)
          .payload(om.writeValueAsString(task.getPayload()).getBytes(UTF8), MediaType.APPLICATION_JSON);
      QueueFactory.getQueue(QUEUE_NAME).addAsync(taskOptions);
    } catch (final JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}

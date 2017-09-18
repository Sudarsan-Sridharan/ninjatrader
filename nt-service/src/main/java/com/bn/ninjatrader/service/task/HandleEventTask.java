package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.event.annotation.Subscribers;
import com.bn.ninjatrader.messaging.Message;
import com.bn.ninjatrader.messaging.listener.MessageListener;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;

/**
 * Called when an event is triggered and passes the event to subscribers.
 *
 * @author bradwee2000@gmail.com
 */
@Singleton
@Path("/tasks/handle-event")
@Produces(MediaType.APPLICATION_JSON)
public class HandleEventTask {
  private static final Logger LOG = LoggerFactory.getLogger(HandleEventTask.class);

  private final Multimap<String, MessageListener> subscribers;

  @Inject
  public HandleEventTask(@Subscribers final Multimap<String, MessageListener> subscribers) {
    this.subscribers = subscribers;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response handleEvent(final Message message) {
    subscribers.get(message.getMessageType()).forEach(subscriber -> subscriber.onMessage(message, LocalDateTime.now()));
    return Response.ok().build();
  }
}

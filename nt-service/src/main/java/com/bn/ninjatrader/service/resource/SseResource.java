package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.event.dispatcher.MessagePublisher;
import com.bn.ninjatrader.service.annotation.Secured;
import com.bn.ninjatrader.service.event.message.SseRegisterMessage;
import com.bn.ninjatrader.service.security.AuthenticatedUser;
import com.bn.ninjatrader.push.store.EventOutputStore;
import com.google.inject.Singleton;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
@Secured
@Path("/sse")
public class SseResource {
  private static final Logger LOG = LoggerFactory.getLogger(SseResource.class);

  private final EventOutputStore eventOutputStore;
  private final MessagePublisher messagePublisher;

  @Inject
  public SseResource(final EventOutputStore eventOutputStore,
                     final MessagePublisher messagePublisher) {
    this.eventOutputStore = eventOutputStore;
    this.messagePublisher = messagePublisher;
  }

  @GET
  @Path("/fire")
  public Response fire(final @Context SecurityContext securityContext) throws IOException {
    final AuthenticatedUser user = (AuthenticatedUser) securityContext.getUserPrincipal();

    final EventOutput eventOutput = eventOutputStore.getOrCreate(user.getUserId());

    eventOutput.write(new OutboundEvent.Builder()
        .name("scanner-dth6m")
        .data(String.class, "Shalalalallala")
        .build());

    eventOutput.write(new OutboundEvent.Builder()
        .data(String.class, "SUBARUUUUUU!!!")
        .build());

    return Response.ok().build();
  }

  @GET
  @Produces(SseFeature.SERVER_SENT_EVENTS)
  public EventOutput getScanResults(final @Context SecurityContext securityContext) {
    final AuthenticatedUser user = (AuthenticatedUser) securityContext.getUserPrincipal();
    LOG.info("Subscriber for {}", user.getUserId());

    final EventOutput eventOutput = eventOutputStore.getOrCreate(user.getUserId());

    messagePublisher.publish(new SseRegisterMessage(user.getUserId()));

    return eventOutput;
  }
}
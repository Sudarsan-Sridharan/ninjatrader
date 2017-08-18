package com.bn.ninjatrader.push;

import com.bn.ninjatrader.push.store.EventOutputStore;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Optional;

/**
 * Push messages (Server-Sent Events) to clients.
 *
 * @author bradwee2000@gmail.com
 */
@Singleton
public class PushPublisher {

  private final EventOutputStore eventOutputStore;

  @Inject
  public PushPublisher(final EventOutputStore eventOutputStore) {
    this.eventOutputStore = eventOutputStore;
  }

  public boolean push(final String userId, final String eventName, final Object data) {
    final Optional<EventOutput> eventOutput = eventOutputStore.get(userId);

    if (!eventOutput.isPresent()) {
      return false;
    }

    try {
      eventOutput.get().write(new OutboundEvent.Builder()
          .name(eventName)
          .mediaType(MediaType.APPLICATION_JSON_TYPE)
          .data(data)
          .build());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return true;
  }
}

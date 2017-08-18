package com.bn.ninjatrader.push.store;

import com.google.common.collect.Maps;
import org.glassfish.jersey.media.sse.EventOutput;

import javax.inject.Singleton;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class EventOutputStore {

  private final Map<String, EventOutput> eventOutputMap = Maps.newConcurrentMap();

  public Optional<EventOutput> get(final String userId) {
    checkNotNull(userId);

    final EventOutput eventOutput = eventOutputMap.get(userId);

    if (eventOutput == null) {
      return Optional.empty();
    }

    // If connection is closed, remove it from store
    if (eventOutput.isClosed()) {
      remove(userId);
      return Optional.empty();
    }

    return Optional.of(eventOutput);
  }

  public EventOutput getOrCreate(final String userId) {
    checkNotNull(userId);

    EventOutput eventOutput = get(userId).orElse(null);

    if (eventOutput == null) {
      eventOutput = new EventOutput();
      eventOutputMap.put(userId, eventOutput);
    }
    return eventOutput;
  }

  public void put(final String userId, final EventOutput eventOutput) {
    this.eventOutputMap.put(userId, eventOutput);
  }

  public void remove(final String userId) {
    eventOutputMap.remove(userId);
  }
}

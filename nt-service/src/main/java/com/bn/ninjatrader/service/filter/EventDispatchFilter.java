package com.bn.ninjatrader.service.filter;

import com.bn.ninjatrader.event.dispatcher.MessagePublisher;
import com.bn.ninjatrader.messaging.Message;
import com.bn.ninjatrader.service.annotation.Event;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class EventDispatchFilter implements ContainerResponseFilter {
  private static final Logger LOG = LoggerFactory.getLogger(EventDispatchFilter.class);

  @Context
  private ResourceInfo resourceInfo;

  private final MessagePublisher messageDispatcher;

  @Inject
  public EventDispatchFilter(final MessagePublisher messageDispatcher) {
    this.messageDispatcher = messageDispatcher;
  }

  @Override
  public void filter(final ContainerRequestContext ctx, final ContainerResponseContext res) throws IOException {
    final Method method = resourceInfo.getResourceMethod();
    if (method == null || !method.isAnnotationPresent(Event.class)) {
      return;
    }

    final Event event = method.getAnnotation(Event.class);

    // If status code does not match, do nothing.
    if (!ArrayUtils.contains(event.statusCodes(), res.getStatus())) {
      return;
    }

    final Object payload = res.getEntity();

    doPublishEventMessageWithPayload(event, payload);
  }

  private void doPublishEventMessageWithPayload(final Event event, final Object payload) {

    // Find constructor that can accept the payload.
    final Constructor constructor = findConstructor(event.messageClass());

    try {
      final Message message = (Message) constructor.newInstance(payload);
      messageDispatcher.publish(message);
    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  private Constructor findConstructor(final Class messageClass) {
    return Lists.newArrayList(messageClass.getConstructors()).stream()
        .filter(c -> c.getParameterCount() == 1)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Single parameter constructor that accepts message payload is not found."));
  }
}

package com.bn.ninjatrader.service.filter;

import com.bn.ninjatrader.event.Message;
import com.bn.ninjatrader.event.dispatcher.MessageDispatcher;
import com.bn.ninjatrader.service.annotation.Event;
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

  private final MessageDispatcher messageDispatcher;

  @Inject
  public EventDispatchFilter(final MessageDispatcher messageDispatcher) {
    this.messageDispatcher = messageDispatcher;
  }

  @Override
  public void filter(final ContainerRequestContext ctx, final ContainerResponseContext res) throws IOException {
    final Method method = resourceInfo.getResourceMethod();
    if (!method.isAnnotationPresent(Event.class)) {
      return;
    }

    final Event event = method.getAnnotation(Event.class);

    // If status code does not match, do nothing.
    if (!ArrayUtils.contains(event.statusCodes(), res.getStatus())) {
      return;
    }

    final Object payload = res.getEntity();

    try {
      final Message message = (Message) event.messageClass().getConstructors()[0]
          .newInstance(payload);
      messageDispatcher.dispatch(message);
    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
}

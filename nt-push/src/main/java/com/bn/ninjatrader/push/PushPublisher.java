package com.bn.ninjatrader.push;

import com.bn.ninjatrader.model.util.ObjectMapperProvider;
import com.bn.ninjatrader.push.config.PushConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pusher.rest.Pusher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;

/**
 * Push messages (Server-Sent Events) to clients.
 *
 * @author bradwee2000@gmail.com
 */
@Singleton
public class PushPublisher {
  private static final Logger LOG = LoggerFactory.getLogger(PushPublisher.class);
  private static final String MESSAGE = "message";
  private final Pusher pusher;
  private final ObjectMapper om;

  @Inject
  public PushPublisher(final PushConfig config, final ObjectMapperProvider objectMapperProvider) {
    this.om = objectMapperProvider.get();

    pusher = new Pusher(config.getAppId(), config.getKey(), config.getSecret());
    pusher.setCluster(config.getCluster());
    pusher.setEncrypted(true);
  }

  public boolean push(final String channel, final String eventName, final Object data) {
    try {
      final String json = om.writeValueAsString(data);
      pusher.trigger(channel, eventName, Collections.singletonMap(MESSAGE, json));
      return true;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

package com.bn.ninjatrader.service.event.handler;

import com.bn.ninjatrader.messaging.listener.MessageListener;
import com.bn.ninjatrader.service.event.message.SseRegisterMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.time.LocalDateTime;

/**
 * Process message once a client registers to receive SSE.
 *
 * @author bradwee2000@gmail.com
 */
@Singleton
public class SseRegisterHandler implements MessageListener<SseRegisterMessage> {
  private static final Logger LOG = LoggerFactory.getLogger(SseRegisterHandler.class);

  @Override
  public void onMessage(final SseRegisterMessage message, final LocalDateTime publishTime) {
    LOG.info("Received Message: {}", message);
  }
}

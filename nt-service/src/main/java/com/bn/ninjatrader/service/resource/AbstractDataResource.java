package com.bn.ninjatrader.service.resource;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;

/**
 * @author bradwee2000@gmail.com
 */
public class AbstractDataResource {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractDataResource.class);

  private final Clock clock;

  @Inject
  public AbstractDataResource(Clock clock) {
    this.clock = clock;
  }

  public Clock getClock() {
    return clock;
  }
}

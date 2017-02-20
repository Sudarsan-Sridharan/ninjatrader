package com.bn.ninjatrader.service.dropwizard.health;

import com.codahale.metrics.health.HealthCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bradwee2000@gmail.com
 */
public class ServiceHealthCheck extends HealthCheck {

  private static final Logger LOG = LoggerFactory.getLogger(ServiceHealthCheck.class);

  @Override
  protected Result check() throws Exception {
    return Result.unhealthy("Cannot connect to mongo db.");
  }
}

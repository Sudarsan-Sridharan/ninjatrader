package com.bn.ninjatrader.service.dropwizard.lifecycle;

import com.bn.ninjatrader.scheduler.JobScheduler;
import io.dropwizard.lifecycle.Managed;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class ManagedJobScheduler implements Managed {

  private final JobScheduler jobScheduler;

  @Inject
  public ManagedJobScheduler(final JobScheduler jobScheduler) {
    this.jobScheduler = jobScheduler;
  }

  @Override
  public void start() throws Exception {
    jobScheduler.start();
  }

  @Override
  public void stop() throws Exception {
    jobScheduler.shutdown();
  }
}

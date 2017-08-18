package com.bn.ninjatrader.scheduler;


import com.bn.ninjatrader.scheduler.exception.JobSchedulerException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.spi.JobFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class QuartzJobScheduler implements JobScheduler {

  private final Scheduler scheduler;

  @Inject
  public QuartzJobScheduler(final SchedulerFactory schedulerFactory,
                            final JobFactory jobFactory) {
    try {
      this.scheduler = schedulerFactory.getScheduler();
      this.scheduler.setJobFactory(jobFactory);
    } catch (SchedulerException e) {
      throw new JobSchedulerException(e);
    }
  }

  @Override
  public void start() {
    try {
      scheduler.start();
    } catch (final SchedulerException e) {
      throw new JobSchedulerException(e);
    }
  }

  @Override
  public void shutdown() {
    try {
      scheduler.shutdown();
    } catch (final SchedulerException e) {
      throw new JobSchedulerException(e);
    }
  }
}

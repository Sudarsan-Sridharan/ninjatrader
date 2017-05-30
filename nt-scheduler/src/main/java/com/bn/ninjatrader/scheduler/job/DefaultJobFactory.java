package com.bn.ninjatrader.scheduler.job;

import com.google.common.collect.Maps;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class DefaultJobFactory implements JobFactory {

  private final Map<Class, Job> jobs = Maps.newHashMap();

  @Inject
  public DefaultJobFactory(final ImportQuotesJob importQuotesJob) {
    jobs.put(importQuotesJob.getClass(), importQuotesJob);
  }

  @Override
  public Job newJob(final TriggerFiredBundle triggerFiredBundle,
                    final Scheduler scheduler) throws SchedulerException {
    final Class jobClass = triggerFiredBundle.getJobDetail().getJobClass();
    return jobs.get(jobClass);
  }
}

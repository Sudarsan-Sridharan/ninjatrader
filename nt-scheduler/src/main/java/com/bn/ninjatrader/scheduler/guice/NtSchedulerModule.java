package com.bn.ninjatrader.scheduler.guice;

import com.bn.ninjatrader.scheduler.JobScheduler;
import com.bn.ninjatrader.scheduler.QuartzJobScheduler;
import com.bn.ninjatrader.scheduler.job.DefaultJobFactory;
import com.bn.ninjatrader.service.client.guice.NtServiceClientModule;
import com.google.inject.AbstractModule;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;

/**
 * @author bradwee2000@gmail.com
 */
public class NtSchedulerModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new NtServiceClientModule());
    bind(SchedulerFactory.class).toInstance(new StdSchedulerFactory());
    bind(JobFactory.class).to(DefaultJobFactory.class);
    bind(JobScheduler.class).to(QuartzJobScheduler.class);
  }
}

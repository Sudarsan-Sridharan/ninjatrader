package com.bn.ninjatrader.scheduler.guice;

import com.bn.ninjatrader.scheduler.JobScheduler;
import com.bn.ninjatrader.scheduler.QuartzJobScheduler;
import com.google.inject.AbstractModule;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author bradwee2000@gmail.com
 */
public class NtSchedulerModule extends AbstractModule {

  @Override
  protected void configure() {

      bind(SchedulerFactory.class).toInstance(new StdSchedulerFactory());

    bind(JobScheduler.class).to(QuartzJobScheduler.class);
  }

  private Properties loadProperties(final String filename) throws IOException {
    final InputStream is = getClass().getResourceAsStream("/" + filename);
    try {
      final Properties properties = new Properties();
      properties.load(is);
      return properties;
    } finally {
      is.close();
    }
  }
}

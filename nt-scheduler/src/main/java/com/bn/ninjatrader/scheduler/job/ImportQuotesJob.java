package com.bn.ninjatrader.scheduler.job;

import com.bn.ninjatrader.dataimport.daily.PseTraderDailyPriceImporter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class ImportQuotesJob implements Job {
  private static final Logger LOG = LoggerFactory.getLogger(ImportQuotesJob.class);

  private final PseTraderDailyPriceImporter quotesImporter;

  @Inject
  public ImportQuotesJob(final PseTraderDailyPriceImporter quotesImporter) {
    this.quotesImporter = quotesImporter;
  }

  @Override
  public void execute(final JobExecutionContext context) throws JobExecutionException {
    final LocalDate fireDate = context.getFireTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

    LOG.info("Fire: {}", fireDate);

    try {
      quotesImporter.importData(fireDate);
    } catch (final Exception e) {
      LOG.warn(e.getMessage());
    }
  }
}

package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.dataimport.daily.PseDailyPriceImporter;
import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.ImmutableMultimap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.dropwizard.servlets.tasks.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;

/**
 * Imports PSE daily quotes to database and runs calculations for the day.
 * To import daily quotes from PSE and store to database:
 * curl -X POST localhost:8081/tasks/import-pse-quotes
 *
 * @author bradwee2000@gmail.com
 */
@Singleton
@Timed
public class ImportPSEDailyQuotesTask extends Task {
  private static final Logger LOG = LoggerFactory.getLogger(ImportPSEDailyQuotesTask.class);

  private final PseDailyPriceImporter importer;
  private final CalcTask calcTask;

  @Inject
  public ImportPSEDailyQuotesTask(final PseDailyPriceImporter importer,
                                  final CalcTask calcTask) {
    super("import-pse-quotes");
    this.importer = importer;
    this.calcTask = calcTask;
  }

  @Override
  public void execute(ImmutableMultimap<String, String> immutableMultimap, PrintWriter printWriter) throws Exception {
    try {
      this.importer.importData();
      LOG.info("Done importing. Calculating.");
      this.calcTask.execute(ImmutableMultimap.of(), printWriter);
      LOG.info("Done calculating.");
      printWriter.append("Done\n");
    } catch (Exception e) {
      printWriter.append("Failed\n");
      throw e;
    }
  }
}

package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.dataimport.daily.PseTraderDailyPriceImporter;
import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.dropwizard.servlets.tasks.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Imports PSE-Trader daily quotes to database and runs calculations for the day.
 *
 * To import daily quotes from PSE-Trader and store to database:
 * curl -X POST localhost:8081/tasks/import-pse-trader-quotes
 *
 * To import data for specific dates:
 * curl -X POST localhost:8081/tasks/import-pse-trader-quotes -d "date={}&date={}"
 *
 * @author bradwee2000@gmail.com
 */
@Singleton
@Timed
public class ImportPSETraderDailyQuotesTask extends Task {
  private static final Logger LOG = LoggerFactory.getLogger(ImportPSETraderDailyQuotesTask.class);
  private static final String DATE_ARG = "date";

  private final PseTraderDailyPriceImporter importer;
  private final Clock clock;

  @Inject
  public ImportPSETraderDailyQuotesTask(final PseTraderDailyPriceImporter importer,
                                        final Clock clock) {
    super("import-pse-trader-quotes");
    this.importer = importer;
    this.clock = clock;
  }

  @Override
  public void execute(final ImmutableMultimap<String, String> args,
                      final PrintWriter printWriter) {
    LOG.info("ARGS: {}", args);
    final ImmutableCollection<String> dateArgs = args.get(DATE_ARG);
    final List<LocalDate> dates;

    // If no date arg passed, use today's date.
    if (dateArgs == null || dateArgs.isEmpty()) {
      dates = Lists.newArrayList(LocalDate.now(clock));
    } else {
      dates = dateArgs.stream()
          .map(dateArg -> LocalDate.parse(dateArg, DateTimeFormatter.BASIC_ISO_DATE))
          .collect(Collectors.toList());
    }
    printWriter.append("Processing dates: " + dates + "\n");

    this.importer.importData(dates);
    printWriter.append("Done\n");
    printWriter.flush();
  }
}

package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.common.util.DateUtil;
import com.bn.ninjatrader.dataimport.daily.PseTraderDailyPriceImporter;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Imports PSE-Trader daily quotes to database and runs calculations for the day.
 *
 * To import daily quotes from PSE-Trader and store to database:
 * curl -X POST localhost:8080/task/import-pse-trader-quotes
 *
 * To import data for specific dates:
 * curl -X POST localhost:8080/task/import-pse-trader-quotes -d "date={}&date={}"
 *
 * @author bradwee2000@gmail.com
 */
@Singleton
@Path("/task/import-pse-trader-quotes")
public class ImportPSETraderDailyQuotesTask {
  private static final Logger LOG = LoggerFactory.getLogger(ImportPSETraderDailyQuotesTask.class);

  private final PseTraderDailyPriceImporter importer;
  private final Clock clock;

  @Inject
  public ImportPSETraderDailyQuotesTask(final PseTraderDailyPriceImporter importer,
                                        final Clock clock) {
    this.importer = importer;
    this.clock = clock;
  }

  @POST
  public void execute(@FormParam("date") final List<String> isoBasicDates) {
    final List<LocalDate> dates;

    // If no date arg passed, use today's date.
    if (isoBasicDates == null || isoBasicDates.isEmpty()) {
      dates = Lists.newArrayList(DateUtil.phNow(clock));
    } else {
      dates = isoBasicDates.stream()
          .map(dateArg -> LocalDate.parse(dateArg, DateTimeFormatter.BASIC_ISO_DATE))
          .collect(Collectors.toList());
    }
    LOG.info("Processing dates: {}", dates);
    importer.importData(dates);
  }
}

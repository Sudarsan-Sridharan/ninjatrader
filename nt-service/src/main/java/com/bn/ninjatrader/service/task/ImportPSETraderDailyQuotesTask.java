package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.service.annotation.Secured;
import com.bn.ninjatrader.common.util.DateUtil;
import com.bn.ninjatrader.dataimport.daily.PseTraderDailyPriceImporter;
import com.bn.ninjatrader.service.model.ImportQuotesRequest;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

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
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
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
  @Secured
  public Response execute(final ImportQuotesRequest req) {
    final List<LocalDate> dates = req == null ? Lists.newArrayList() : req.getDates();

    if (dates.isEmpty()) {
      dates.add(DateUtil.phNow(clock));
    }

    LOG.info("Processing dates: {}", dates);
    importer.importData(dates);

    return Response.ok().build();
  }
}

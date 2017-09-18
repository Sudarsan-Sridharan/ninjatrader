package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.common.util.DateUtil;
import com.bn.ninjatrader.dataimport.daily.PseTraderDailyPriceImporter;
import com.bn.ninjatrader.common.model.DailyQuote;
import com.bn.ninjatrader.service.annotation.Event;
import com.bn.ninjatrader.service.annotation.Secured;
import com.bn.ninjatrader.service.event.message.ImportedFullPricesMessage;
import com.bn.ninjatrader.common.rest.ImportQuotesRequest;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import static com.bn.ninjatrader.common.type.Role.ADMIN;

/**
 * Imports PSE-Trader daily quotes to database and runs calculations for the day.
 *
 * To import daily quotes from PSE-Trader and store to database:
 * curl -X POST localhost:8080/tasks/import-pse-trader-quotes
 *
 * To import data for specific dates:
 * curl -X POST localhost:9000/tasks/import-pse-trader-quotes -d {\"dates\":[\"20170824\"]} -H "Content-Type: application/json" --cookie "au=XXX"
 *
 * @author bradwee2000@gmail.com
 */
@Singleton
@Secured(ADMIN)
@Path("/tasks/import-pse-trader-quotes")
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

  @GET
  @Event(messageClass = ImportedFullPricesMessage.class)
  public Response importDailyQuotes() {
    final List<LocalDate> dates = Lists.newArrayList(DateUtil.phNow(clock));
    return importQuotesForDates(dates);
  }

  @POST
  @Event(messageClass = ImportedFullPricesMessage.class)
  public Response execute(final ImportQuotesRequest req) {
    final List<LocalDate> dates = req == null ? Lists.newArrayList() : req.getDates();

    if (dates.isEmpty()) {
      dates.add(DateUtil.phNow(clock));
    }

    return importQuotesForDates(dates);
  }

  private Response importQuotesForDates(final List<LocalDate> dates) {
    LOG.info("Processing dates: {}", dates);
    final List<DailyQuote> importedQuotes = importer.importData(dates);
    return Response.ok(importedQuotes).build();
  }
}

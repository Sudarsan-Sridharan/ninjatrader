package com.bn.ninjatrader.service.task;

import com.bn.ninjatrader.dataimport.daily.PseDailyPriceImporter;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.time.Clock;
import java.time.LocalDate;

/**
 * Imports PSE daily quotes to database and runs calculations for the day.
 * To import daily quotes from PSE and store to database:
 * curl -X POST localhost:8081/tasks/import-pse-quotes
 *
 * @author bradwee2000@gmail.com
 */
@Singleton
@Path("/task/import-pse-quotes")
public class ImportPSEDailyQuotesTask {
  private static final Logger LOG = LoggerFactory.getLogger(ImportPSEDailyQuotesTask.class);

  private final PseDailyPriceImporter importer;
  private final Clock clock;

  @Inject
  public ImportPSEDailyQuotesTask(final PseDailyPriceImporter importer,
                                  final Clock clock) {
    this.importer = importer;
    this.clock = clock;
  }

  @POST
  public Response importPseDailyQuotes() {
    this.importer.importData(LocalDate.now(clock));
    return Response.noContent().build();
  }
}

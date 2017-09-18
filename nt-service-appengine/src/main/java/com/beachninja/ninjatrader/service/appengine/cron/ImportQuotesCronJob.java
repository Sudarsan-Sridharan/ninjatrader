package com.beachninja.ninjatrader.service.appengine.cron;

import com.bn.ninjatrader.service.task.ImportPSETraderDailyQuotesTask;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
@Path("/cron/import-quotes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ImportQuotesCronJob {
  private static final Logger LOG = LoggerFactory.getLogger(ImportQuotesCronJob.class);

  @Context
  private ResourceContext resourceContext;

  @GET
  public Response run() throws IOException, URISyntaxException {
    return resourceContext.getResource(ImportPSETraderDailyQuotesTask.class).importDailyQuotes();
  }
}

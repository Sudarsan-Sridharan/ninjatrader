package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.common.model.DailyQuote;
import com.bn.ninjatrader.event.dispatcher.MessagePublisher;
import com.bn.ninjatrader.service.annotation.Secured;
import com.bn.ninjatrader.service.event.message.ImportedFullPricesMessage;
import com.bn.ninjatrader.simulation.scanner.ScanRequest;
import com.google.common.collect.Lists;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
//TODO Remove
@Singleton
@Secured
@Consumes(MediaType.APPLICATION_JSON)
@Path("/test-event")
public class TestEventResource {
  private static final Logger LOG = LoggerFactory.getLogger(TestEventResource.class);

  private final MessagePublisher messagePublisher;

  @Inject
  public TestEventResource(final MessagePublisher messagePublisher) {
    this.messagePublisher = messagePublisher;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response fire(final @Context SecurityContext securityContext) throws IOException {

    final List<DailyQuote> quotes = Lists.newArrayList(new DailyQuote("MEG", LocalDate.now().plusDays(1), Math.random(), 2, 3, 4, 5000));

    messagePublisher.publish(new ImportedFullPricesMessage(quotes));

    return Response.ok(quotes).build();
  }

  @POST
  public Response lala(final ScanRequest req) {
    LOG.info("Received Request: {}", req);
    return Response.ok().build();
  }
}
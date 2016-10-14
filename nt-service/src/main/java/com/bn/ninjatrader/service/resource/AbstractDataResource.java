package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.request.FindRequest;
import com.bn.ninjatrader.service.indicator.IchimokuService;
import com.bn.ninjatrader.service.model.ResourceRequest;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
@Path("/ichimoku")
@Produces(MediaType.APPLICATION_JSON)
public class AbstractDataResource {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractDataResource.class);

  private final IchimokuService ichimokuService;
  private final Clock clock;

  @Inject
  public AbstractDataResource(IchimokuService ichimokuService, Clock clock) {
    this.ichimokuService = ichimokuService;
    this.clock = clock;
  }

  @GET
  @Path("/{symbol}")
  public List<Ichimoku> getIchimoku(@BeanParam ResourceRequest req) {
    LOG.info("REQUEST: {}" , req);

    FindRequest findRequest = FindRequest.findSymbol(req.getSymbol())
        .timeframe(req.getTimeFrame().orElse(TimeFrame.ONE_DAY))
        .from(req.getFrom().orElse(LocalDate.now(clock).minusYears(2)))
        .to(req.getTo().orElse(LocalDate.now(clock)));

    List<Ichimoku> ichimokuList = ichimokuService.find(findRequest);

    return ichimokuList;
  }
}

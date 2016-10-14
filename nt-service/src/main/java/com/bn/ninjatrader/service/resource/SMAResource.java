package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.service.indicator.IchimokuService;
import com.bn.ninjatrader.service.model.IchimokuResponse;
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
import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
@Path("/ichimoku")
@Produces(MediaType.APPLICATION_JSON)
public class SMAResource extends AbstractDataResource {

  private static final Logger LOG = LoggerFactory.getLogger(SMAResource.class);

  private final IchimokuService ichimokuService;

  @Inject
  public SMAResource(IchimokuService ichimokuService, Clock clock) {
    super(clock);
    this.ichimokuService = ichimokuService;
  }

  @GET
  @Path("/{symbol}")
  public IchimokuResponse getIchimoku(@BeanParam ResourceRequest req) {
    List<Ichimoku> ichimokuList = ichimokuService.find(req.toFindRequest(getClock()));
    return new IchimokuResponse().setValues(ichimokuList);
  }
}

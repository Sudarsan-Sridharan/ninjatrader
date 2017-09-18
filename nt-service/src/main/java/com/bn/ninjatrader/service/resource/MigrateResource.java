package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.common.model.Price;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/migrate")
public class MigrateResource {
  private static final Logger LOG = LoggerFactory.getLogger(MigrateResource.class);

  private final PriceDao priceDao;

  @Inject
  public MigrateResource(final PriceDao priceDao) {
    this.priceDao = priceDao;
  }

  @Path("/price/{symbol}")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public Response migrate(List<Price> prices, @PathParam("symbol") String symbol) {
    LOG.info("{}", prices);
    priceDao.savePrices(prices).withSymbol(symbol).withTimeFrame(TimeFrame.ONE_DAY).now();

    return Response.ok("Success").build();
  }

  @GET
  public Response hello() {
    return Response.ok("Hello").build();
  }
}
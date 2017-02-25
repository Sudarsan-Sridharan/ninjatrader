package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.request.FindPriceRequest;
import com.bn.ninjatrader.service.model.PriceRequest;
import com.bn.ninjatrader.service.model.PriceResponse;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
@Path("/price")
public class PriceResource extends AbstractDataResource {
  private static final Logger LOG = LoggerFactory.getLogger(PriceResource.class);

  private final PriceDao priceDao;
  private final Clock clock;

  @Inject
  public PriceResource(final PriceDao priceDao, final Clock clock) {
    super(clock);
    this.priceDao = priceDao;
    this.clock = clock;
  }

  @GET
  @Path("/{symbol}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPrices(@BeanParam final PriceRequest priceRequest) {
    final FindPriceRequest req = FindPriceRequest.forSymbol(priceRequest.getSymbol())
        .timeframe(priceRequest.getTimeFrame().orElse(TimeFrame.ONE_DAY))
        .from(priceRequest.getFrom().orElse(LocalDate.now(clock).minusYears(2)))
        .to(priceRequest.getTo().orElse(LocalDate.now(clock)));

    final List<Price> prices = priceDao.find(req);
    return Response.ok(createPriceResponse(prices))
        .header("Access-Control-Allow-Origin", "*")
        .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
        .header("Access-Control-Allow-Methods", "GET")
        .build();
  }

  private PriceResponse createPriceResponse(final List<Price> prices) {
    final PriceResponse response = new PriceResponse();
    if (!prices.isEmpty()) {
      response.setFromDate(prices.get(0).getDate());
      response.setToDate(prices.get(prices.size() - 1).getDate());
      response.setPriceList(prices);
    }
    return response;
  }
}
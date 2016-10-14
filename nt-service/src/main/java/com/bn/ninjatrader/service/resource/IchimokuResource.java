package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.rest.PriceResponse;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.common.util.PriceUtil;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.request.FindRequest;
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
@Path("/price")
@Produces(MediaType.APPLICATION_JSON)
public class IchimokuResource {

  private static final Logger LOG = LoggerFactory.getLogger(IchimokuResource.class);

  private final PriceDao priceDao;
  private final Clock clock;

  @Inject
  public IchimokuResource(PriceDao priceDao, Clock clock) {
    this.priceDao = priceDao;
    this.clock = clock;
  }

  @GET
  @Path("/{symbol}")
  public PriceResponse getPrices(@BeanParam ResourceRequest req) {
    LOG.info("REQUEST: {}" , req);

    FindRequest findRequest = FindRequest.findSymbol(req.getSymbol())
        .timeframe(req.getTimeFrame().orElse(TimeFrame.ONE_DAY))
        .from(req.getFrom().orElse(LocalDate.now(clock).minusYears(2)))
        .to(req.getTo().orElse(LocalDate.now(clock)));

    List<Price> prices = priceDao.find(findRequest);

    return createPriceResponse(prices);
  }

  private PriceResponse createPriceResponse(List<Price> prices) {
    PriceResponse response = new PriceResponse();
    if (!prices.isEmpty()) {
      response.setFromDate(prices.get(0).getDate());
      response.setToDate(prices.get(prices.size() - 1).getDate());
      response.setPriceList(prices);
      response.setPriceSummary(PriceUtil.createSummary(prices));
    }
    return response;
  }
}

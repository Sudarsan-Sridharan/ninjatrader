package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.service.model.PriceResponse;
import com.bn.ninjatrader.common.util.PriceUtil;
import com.bn.ninjatrader.model.dao.PriceDao;
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
@Path("/price")
@Produces(MediaType.APPLICATION_JSON)
public class PriceResource extends AbstractDataResource {
  private static final Logger LOG = LoggerFactory.getLogger(PriceResource.class);

  private final PriceDao priceDao;

  @Inject
  public PriceResource(final PriceDao priceDao, final Clock clock) {
    super(clock);
    this.priceDao = priceDao;
  }

  @GET
  @Path("/{symbol}")
  public PriceResponse getPrices(@BeanParam final ResourceRequest req) {
    final List<Price> prices = priceDao.find(req.toFindRequest(getClock()));
    return createPriceResponse(prices);
  }

  private PriceResponse createPriceResponse(final List<Price> prices) {
    final PriceResponse response = new PriceResponse();
    if (!prices.isEmpty()) {
      response.setFromDate(prices.get(0).getDate());
      response.setToDate(prices.get(prices.size() - 1).getDate());
      response.setPriceList(prices);
      response.setPriceSummary(PriceUtil.createSummary(prices));
    }
    return response;
  }
}

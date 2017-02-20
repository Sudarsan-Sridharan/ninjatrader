package com.bn.ninjatrader.service.migrate;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.PriceUtil;
import com.bn.ninjatrader.model.appengine.PriceDocument;
import com.bn.ninjatrader.model.appengine.dao.PriceDaoGae;
import com.bn.ninjatrader.model.appengine.request.SavePriceRequest;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.service.model.PriceResponse;
import com.bn.ninjatrader.service.model.ResourceRequest;
import com.bn.ninjatrader.service.resource.AbstractDataResource;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.googlecode.objectify.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.Clock;
import java.util.List;
import java.util.Map;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
@Path("/price2")
public class PriceResource2 extends AbstractDataResource {
  private static final Logger LOG = LoggerFactory.getLogger(PriceResource2.class);

  private final PriceDao priceDao;
  private final PriceDaoGae priceDaoGae;

  @Inject
  public PriceResource2(final PriceDao priceDao, final PriceDaoGae priceDaoGae, final Clock clock) {
    super(clock);
    this.priceDao = priceDao;
    this.priceDaoGae = priceDaoGae;
  }

  @GET
  @Path("/migrate/{symbol}")
  @Produces(MediaType.TEXT_HTML)
  public String migrate(@BeanParam final ResourceRequest req) {
    final List<Price> prices = priceDao.find(req.toFindRequest(getClock()));
    LOG.info("Prices for {} - {}", req.getSymbol(), prices);
    final Map<Key<PriceDocument>, PriceDocument> saved = priceDaoGae.save(SavePriceRequest.forSymbol(req.getSymbol()).addPrices(prices));

    return saved.toString();
  }

  @GET
  @Path("/list/{symbol}")
  @Produces(MediaType.APPLICATION_JSON)
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

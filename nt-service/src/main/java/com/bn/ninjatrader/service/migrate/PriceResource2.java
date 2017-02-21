package com.bn.ninjatrader.service.migrate;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.PriceUtil;
import com.bn.ninjatrader.model.appengine.request.SavePriceRequest;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.datastore.PriceDaoDatastore;
import com.bn.ninjatrader.model.request.FindRequest;
import com.bn.ninjatrader.service.model.PriceResponse;
import com.bn.ninjatrader.service.model.ResourceRequest;
import com.bn.ninjatrader.service.resource.AbstractDataResource;
import com.google.common.collect.Lists;
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
import java.util.Collections;
import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
@Deprecated
@Singleton
@Path("/price2")
public class PriceResource2 extends AbstractDataResource {
  private static final Logger LOG = LoggerFactory.getLogger(PriceResource2.class);

  private final PriceDao priceDao;
  private final PriceDaoDatastore priceDaoGae;

  @Inject
  public PriceResource2(final PriceDao priceDao, final PriceDaoDatastore priceDaoGae, final Clock clock) {
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
    priceDaoGae.save(SavePriceRequest.forSymbol(req.getSymbol()).addPrices(prices));

    final StringBuilder sb = new StringBuilder();

    final List<String> symbols = Lists.newArrayList(priceDao.findAllSymbols());
    Collections.sort(symbols);
    for (String symbol : symbols)
    sb.append("<a style=\"display:block;float:left;padding:3px;\" href=\"http://localhost:8080/price2/migrate/").append(symbol).append("\">").append(symbol).append("</a>");

    return sb.toString();
  }

  @GET
  @Path("/migrateall")
  @Produces(MediaType.TEXT_HTML)
  public String migrateAll() {
    final List<String> symols = Lists.newArrayList(priceDao.findAllSymbols());

    Collections.sort(symols);

    for (final String symbol: symols) {
      LOG.info(symbol);
    }

    for (final String symbol: symols) {
      final List<Price> prices = priceDao.find(FindRequest.findSymbol(symbol).from(LocalDate.now().minusYears(200)).to(LocalDate.now()));
      priceDaoGae.save(SavePriceRequest.forSymbol(symbol).addPrices(prices));
      LOG.info("Prices for {} - {}", symbol, prices.size());
    }
    return "Done.";
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

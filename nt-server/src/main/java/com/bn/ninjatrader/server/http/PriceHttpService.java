package com.bn.ninjatrader.server.http;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.rest.PriceResponse;
import com.bn.ninjatrader.common.type.Period;
import com.bn.ninjatrader.common.util.PriceUtil;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.WeeklyPriceDao;
import com.bn.ninjatrader.model.request.FindRequest;
import com.google.inject.Inject;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.inject.Singleton;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Created by Brad on 5/3/16.
 */
@Singleton
@At("/price")
@Service
public class PriceHttpService {

  private static final Logger log = LoggerFactory.getLogger(PriceHttpService.class);

  @Inject
  private PriceDao priceDao;

  @Inject
  private WeeklyPriceDao weeklyPriceDao;

  @At("/:symbol/daily")
  @Get
  public Reply<?> getDailyPrices(@Named("symbol") String symbol) {
    log.debug("Request to get daily prices for symbol: {}", symbol);
    try {
      Optional<PriceResponse> foundPriceData = getPriceResponse(symbol, Period.DAILY);

      if (!foundPriceData.isPresent()) {
        return Reply.saying().notFound();
      }

      return Reply.with(foundPriceData.get()).as(Json.class);
    } catch (Exception e) {
      log.error("getDailyPrices error.", e);
      return Reply.saying().error();
    }
  }

  @At("/:symbol/weekly")
  @Get
  public Reply<?> getWeeklyPrices(@Named("symbol") String symbol) {
    log.debug("Request to get weekly prices for symbol: {}", symbol);
    try {
      Optional<PriceResponse> foundPriceData = getPriceResponse(symbol, Period.WEEKLY);

      if (!foundPriceData.isPresent()) {
        return Reply.saying().notFound();
      }

      return Reply.with(foundPriceData.get()).as(Json.class);
    } catch (Exception e) {
      log.error("getWeeklyPrices error.", e);
      return Reply.saying().error();
    }
  }

  /**
   * Get PriceData of boardlot
   * @param symbol
   * @return
   */
  public Optional<PriceResponse> getPriceResponse(String symbol) {
    return getPriceResponse(symbol, Period.DAILY);
  }

  public Optional<PriceResponse> getPriceResponse(String symbol, Period period) {
    LocalDate fromDate = period == Period.WEEKLY ? LocalDate.now().minusYears(2) : LocalDate.now().minusMonths(6);
    LocalDate toDate = LocalDate.now();

    PriceDao dao = period == Period.WEEKLY ? weeklyPriceDao : priceDao;

    List<Price> prices = dao.find(FindRequest.forSymbol(symbol).from(fromDate).to(toDate));
    log.debug("Found {} prices for {}", prices.size(), symbol);

    // If no document, return empty
    if (prices.isEmpty()) {
      return Optional.empty();
    }

    PriceResponse response = new PriceResponse();
    response.setFromDate(prices.get(0).getDate());
    response.setToDate(prices.get(prices.size() - 1).getDate());
    response.setPriceList(prices);
    response.setPriceSummary(PriceUtil.createSummary(prices));

    return Optional.of(response);
  }
}

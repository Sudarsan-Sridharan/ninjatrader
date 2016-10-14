package com.bn.ninjatrader.server.http;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.rest.PriceResponse;
import com.bn.ninjatrader.common.type.Period;
import com.bn.ninjatrader.common.util.DateUtil;
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
public class SMAHttpService {

  private static final Logger log = LoggerFactory.getLogger(SMAHttpService.class);

  @Inject
  private PriceDao priceDao;

  @Inject
  private WeeklyPriceDao weeklyPriceDao;

  @At("/daily/:symbol")
  @Get
  public Reply<?> getDailyPrices(@Named("symbol") String symbol) {
    return processRequest(Period.DAILY, symbol, "", "");
  }

  @At("/daily/:symbol/from/:from/to/:to")
  @Get
  public Reply<?> getDailyPricesFromTo(@Named("symbol") String symbol,
                                         @Named("from") String from,
                                         @Named("to") String to) {
    return processRequest(Period.DAILY, symbol, from, to);
  }

  @At("/weekly/:symbol")
  @Get
  public Reply<?> getWeeklyPrices(@Named("symbol") String symbol) {
    return processRequest(Period.WEEKLY, symbol, "", "");
  }

  public Reply<?> processRequest(Period period, String symbol, String from , String to) {
    try {
      Optional<PriceResponse> foundPrices = findPrices(period, symbol, from, to);

      if (!foundPrices.isPresent()) {
        return Reply.saying().noContent();
      }

      return Reply.with(foundPrices.get()).as(Json.class);
    } catch (Exception e) {
      log.error("Failed to process request." , e);
      return Reply.saying().error();
    }
  }

  public Optional<PriceResponse> findPrices(Period period, String symbol, String from , String to) {
    log.debug("Request for Prices [period={}] [symbol={}] [from={}] [to={}]", period, symbol, from, to);

    PriceDao dao = period == Period.WEEKLY ? weeklyPriceDao : priceDao;

    LocalDate fromDate = DateUtil.parse(from, LocalDate.now().minusYears(2));
    LocalDate toDate = DateUtil.parse(to, LocalDate.now());

    List<Price> prices = dao.find(FindRequest.findSymbol(symbol).from(fromDate).to(toDate));

    if (prices.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(createPriceResponse(prices));
  }

  public PriceResponse createPriceResponse(List<Price> prices) {
    PriceResponse response = new PriceResponse();
    response.setFromDate(prices.get(0).getDate());
    response.setToDate(prices.get(prices.size() - 1).getDate());
    response.setPriceList(prices);
    response.setPriceSummary(PriceUtil.createSummary(prices));

    return response;
  }
}

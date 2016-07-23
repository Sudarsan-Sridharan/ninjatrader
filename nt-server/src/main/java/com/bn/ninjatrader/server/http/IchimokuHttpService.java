package com.bn.ninjatrader.server.http;

import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.common.data.Stock;
import com.bn.ninjatrader.server.service.IchimokuService;
import com.bn.ninjatrader.server.service.IchimokuWeeklyService;
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

/**
 * Created by Brad on 5/3/16.
 */
@Singleton
@At("/ichimoku")
@Service
public class IchimokuHttpService {

  private static final Logger log = LoggerFactory.getLogger(IchimokuHttpService.class);

  @Inject
  private IchimokuService ichimokuService;

  @Inject
  private IchimokuWeeklyService ichimokuWeeklyService;

  @At("/daily/:symbol")
  @Get
  public Reply<?> getDailyIchimoku(@Named("symbol") String symbol) {
    log.debug("Request for Daily Ichimoku for symbol: {}", symbol);
    try {
      LocalDate fromDate = LocalDate.now().minusMonths(6);
      LocalDate toDate = LocalDate.now();

      List<Ichimoku> ichimokuList = ichimokuService.getIchimoku(symbol, fromDate, toDate);

      return toReply(ichimokuList);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return Reply.saying().error();
    }
  }

  @At("/weekly/:symbol")
  @Get
  public Reply<?> getWeeklyIchimoku(@Named("symbol") String symbol) {
    log.debug("Request for Weekly Ichimoku for symbol: {}", symbol);
    try {
      LocalDate fromDate = LocalDate.now().minusYears(2);
      LocalDate toDate = LocalDate.now();

      List<Ichimoku> ichimokuList = ichimokuWeeklyService.getIchimoku(symbol, fromDate, toDate);

      return toReply(ichimokuList);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return Reply.saying().error();
    }
  }

  @At("/hits")
  @Get
  public Reply<?> getStocksWithKijunCross() {
    return replyWithKijunCrossHits(ichimokuWeeklyService);
  }

  @At("/weeklyhits")
  @Get
  public Reply<?> getStocksWithWeeklyKijunCross() {
    return replyWithKijunCrossHits(ichimokuWeeklyService);
  }

  private Reply<?> replyWithKijunCrossHits(IchimokuService service) {
    try {
      List<Stock> stockList = service.getStocksWithKijunCross();
      return toReply(stockList);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return Reply.saying().error();
    }
  }

  private Reply<?> toReply(List<?> results) {
    if (results.isEmpty()) {
      return Reply.saying().noContent();
    }
    return Reply.with(results).as(Json.class);
  }
}

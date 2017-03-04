package com.bn.ninjatrader.thirdparty.pse;

import com.bn.ninjatrader.common.util.DateFormats;
import com.bn.ninjatrader.model.entity.DailyQuote;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Lists;
import com.google.inject.Singleton;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Brad on 6/22/16.
 */
@Singleton
public class PseService {

  private static final String ALL_STOCK_URL =
      "http://www.pse.com.ph/stockMarket/home.html?method=getSecuritiesAndIndicesForPublic&ajax=true";
  private static final String ALL_TOP_STOCKS =
      "http://www.pse.com.ph/stockMarket/home.html?method=getTopSecurity&ajax=true&limit=9999";
  private static final String MARKET_DETAILS =
      "http://www.pse.com.ph/stockMarket/home.html?method=fetchIndicesDetails&ajax=true";
  private static final String FIND_SECURITY_URL =
      "http://www.pse.com.ph/stockMarket/home.html?method=findSecurityOrCompany&ajax=true&start=0&limit=1&query=%s";
  private static final String GET_QUOTE_URL =
      "http://www.pse.com.ph/stockMarket/companyInfo.html?method=fetchHeaderData&ajax=true&symbol=%s";
  private static final Logger LOG = LoggerFactory.getLogger(PseService.class);

  private ObjectMapper om = new ObjectMapper()
      .configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  public PseAllStockUpdate getAllStockIndices() throws IOException {
    final String json = Request.Get(ALL_STOCK_URL)
        .connectTimeout(10000)
        .socketTimeout(10000)
        .execute()
        .returnContent().asString();

    List<PseStock> stockList;
    try {
      stockList = Lists.newArrayList(om.readValue(json, PseStock[].class));
    } catch (Exception e) {
      LOG.error("Error parsing json: {}", json);
      LOG.error(e.getMessage(), e);
      throw e;
    }

    // remove 1st one -- the header contains the last updated date.
    PseStock header = stockList.remove(0);
    LocalDateTime lastUpdated = LocalDateTime.parse(header.getName(), DateFormats.PSE_DATE_TIME_FORMAT);

    return new PseAllStockUpdate(lastUpdated, stockList);
  }

  public List<PseStock> getAllTopStocks() throws IOException {
    final String json = Request.Get(ALL_TOP_STOCKS)
        .connectTimeout(10000)
        .socketTimeout(10000)
        .execute()
        .returnContent().asString();
    final ResponseResult<PseStock> result = om.readValue(json, new TypeReference<ResponseResult<PseStock>>() {});
    return result.getRecords();
  }

  public ResponseResult<PseMarketDetail> getMarketDetails() throws IOException {
    final String json = Request.Get(MARKET_DETAILS)
        .connectTimeout(10000)
        .socketTimeout(10000)
        .execute()
        .returnContent().asString();
    final ResponseResult<PseMarketDetail> result = om.readValue(json, new TypeReference<ResponseResult<PseMarketDetail>>() {});
    return result;
  }

  public List<DailyQuote> getAllDailyQuotes() {
    try {
      final ExecutorService executor = Executors.newFixedThreadPool(10);
      final List<DailyQuote> quotes = Collections.synchronizedList(Lists.newArrayList());

      LocalDate pseDate;
      List<PseStock> pseStocks;
      try {
        // Attempt to get date and stocks from PSE All Stock Indices
        final PseAllStockUpdate pseAllStockUpdate = getAllStockIndices();
        pseDate = pseAllStockUpdate.getLastUpdateDate().toLocalDate();
        pseStocks = pseAllStockUpdate.getStocks();
      } catch (final Exception e) {
        LOG.warn("Failed to get data from AllStockIndices. Attempting to get from AllTopStocks.");
        // If Fail, attempt to get date from market details and stocks for All Top Stocks
        pseDate = getMarketDetails().getRecords().get(0).getTradingTime();
        pseStocks = getAllTopStocks();
      }

      final LocalDate date = pseDate;
      for (final PseStock stock : pseStocks) {
        executor.execute(() -> {
          final Optional<DailyQuote> quote = getDailyQuote(stock.getSymbol());
          if (quote.isPresent()) {
            quote.get().setDate(date);
            quotes.add(quote.get());
            LOG.info("{}", quote.orElse(null));
          }
        });
      }

      // Wait to finish
      executor.shutdown();
      try {
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
      } catch (InterruptedException e) {
        LOG.error(e.getMessage(), e);
        throw new RuntimeException(e);
      }
      return quotes;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public Optional<DailyQuote> getDailyQuote(String symbol) {
    try {
      String url = String.format(GET_QUOTE_URL, symbol);
      String json = Request.Get(url)
          .connectTimeout(10000)
          .socketTimeout(10000)
          .execute()
          .returnContent().asString();

      PseQuote.Response results = om.readValue(json, PseQuote.Response.class);

      if (results.getRecords().isEmpty()) {
        return Optional.empty();
      }

      DailyQuote quote = results.getRecords().get(0).toDailyQuote();
      return Optional.of(quote);
    } catch (Exception e) {
      LOG.error("Error getting PSE price for symbol: {}", symbol);
      LOG.error(e.getMessage(), e);
      return Optional.empty();
    }
  }

  public static void main(String args[]) throws IOException {
    PseService app = new PseService();
    app.getAllDailyQuotes();
  }
}

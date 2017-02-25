package com.bn.ninjatrader.thirdparty.pse;

import com.bn.ninjatrader.model.entity.DailyQuote;
import com.bn.ninjatrader.common.util.DateFormats;
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
  private static final String FIND_SECURITY_URL =
      "http://www.pse.com.ph/stockMarket/home.html?method=findSecurityOrCompany&ajax=true&start=0&limit=1&query=%s";
  private static final String GET_QUOTE_URL =
      "http://www.pse.com.ph/stockMarket/companyInfo.html?method=fetchHeaderData&ajax=true&symbol=%s";
  private static final Logger log = LoggerFactory.getLogger(PseService.class);

  private ObjectMapper om = new ObjectMapper()
      .configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  public PseAllStockUpdate getAllStockIndices() throws IOException {
    String json = Request.Get(ALL_STOCK_URL)
        .connectTimeout(10000)
        .socketTimeout(10000)
        .execute()
        .returnContent().asString();

    List<PseStock> stockList;
    try {
      stockList = Lists.newArrayList(om.readValue(json, PseStock[].class));
    } catch (Exception e) {
      log.error("Error parsing json: {}", json);
      log.error(e.getMessage(), e);
      throw e;
    }

    // remove 1st one -- the header contains the last updated date.
    PseStock header = stockList.remove(0);
    LocalDateTime lastUpdated = LocalDateTime.parse(header.getName(), DateFormats.PSE_DATE_TIME_FORMAT);

    return new PseAllStockUpdate(lastUpdated, stockList);
  }

  public List<DailyQuote> getAllDailyQuotes() {
    try {
      ExecutorService executor = Executors.newFixedThreadPool(10);
      List<DailyQuote> quotes = Collections.synchronizedList(Lists.newArrayList());
      PseAllStockUpdate pseAllStockUpdate = getAllStockIndices();
      LocalDate lastUpdate = pseAllStockUpdate.getLastUpdateDate().toLocalDate();

      for (PseStock stock : pseAllStockUpdate.getStocks()) {
        executor.execute(new Runnable() {
          @Override
          public void run() {
            Optional<DailyQuote> quote = getDailyQuote(stock.getSymbol());
            if (quote.isPresent()) {
              quote.get().setDate(lastUpdate);
              quotes.add(quote.get());
              log.info("{}", quote.orElse(null));
            }
          }
        });
      }

      // Wait to finish
      executor.shutdown();
      try {
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
      } catch (InterruptedException e) {
        log.error(e.getMessage(), e);
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
      log.error("Error getting PSE price for symbol: {}", symbol);
      log.error(e.getMessage(), e);
      return Optional.empty();
    }
  }

  public int getSecurityId(String symbol) throws IOException {
    String url = String.format(FIND_SECURITY_URL, symbol);
    try {
      String json = Request.Get(url)
          .connectTimeout(10000)
          .socketTimeout(10000)
          .execute()
          .returnContent().asString();
      PseSecurity.Response results = om.readValue(json, PseSecurity.Response.class);
      if (results.getRecords().isEmpty()) {
        return 0;
      }
      return results.getRecords().get(0).getSecurityId();

    } catch (Exception e) {
      log.error("Error: {}", url);
      log.error(e.getMessage());
      // Most likely wrong symbol
      return 0;
    }
  }

  public static void main(String args[]) throws IOException {
    PseService app = new PseService();
    app.getAllDailyQuotes();
  }
}

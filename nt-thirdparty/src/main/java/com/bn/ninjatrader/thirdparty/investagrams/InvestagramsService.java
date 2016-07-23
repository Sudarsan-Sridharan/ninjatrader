package com.bn.ninjatrader.thirdparty.investagrams;

import com.bn.ninjatrader.common.data.DailyQuote;
import com.bn.ninjatrader.common.util.NumUtil;
import com.google.common.collect.Lists;
import com.google.inject.Singleton;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Brad on 6/25/16.
 */
@Singleton
public class InvestagramsService {

  private static final Logger log = LoggerFactory.getLogger(InvestagramsService.class);

  public static final String DAILY_QUOTES_URL = "https://www.investagrams.com/Stock/RealTimeMonitoring";
  public static final String HISTORICAL_QUOTES_URL = "https://www.investagrams.com/Stock/";
  private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd, yyyy");

  public List<DailyQuote> getDailyQuotes() {
    try {
      Document doc = getDocumentFromUrl(DAILY_QUOTES_URL);
      Elements rows = doc.getElementById("StockQuoteTable").getElementsByTag("tbody").get(0).children();

      List<DailyQuote> quotes = Lists.newArrayList();
      for (Element row : rows) {
        String symbol = row.attr("id");
        Elements cols = row.getElementsByTag("td");

        double open = NumUtil.toDouble(cols.get(5).text());
        double low = NumUtil.toDouble(cols.get(6).text());
        double high = NumUtil.toDouble(cols.get(7).text());
        double close = NumUtil.toDouble(cols.get(8).text());
        long volume = NumUtil.toLong(cols.get(9).text());

        DailyQuote quote = new DailyQuote(symbol, LocalDate.now(), open, high, low, close, volume);
        log.info("{}", quote);
        quotes.add(quote);
      }
      return quotes;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public List<DailyQuote> getHistoricalQuotes(String symbol) {
    List<DailyQuote> results = Lists.newArrayList();
    String url = HISTORICAL_QUOTES_URL + symbol;

    Document doc = getDocumentFromUrl(url);

    Elements rows = doc.getElementById("HistoricalDataTable").getElementsByTag("tbody").get(0).children();

    for (Element row :rows) {
      Elements cols = row.getElementsByTag("td");

      LocalDate date = LocalDate.parse(cols.get(0).text(), dtf);
      double close = NumUtil.toDouble(cols.get(1).text());
      double open = NumUtil.toDouble(cols.get(4).text());
      double low = NumUtil.toDouble(cols.get(5).text());
      double high = NumUtil.toDouble(cols.get(6).text());
      long volume = NumUtil.toLong(cols.get(7).text());

      DailyQuote quote = new DailyQuote(symbol, date, open, high, low, close, volume);
      results.add(quote);
    }
    log.info("Found {} quotes for symbol: {}", results.size(), symbol);
    return results;
  }

  private Document getDocumentFromUrl(String url) {
    try {
      return Jsoup.connect(url).timeout(15000).get();
    } catch (IOException e) {
      log.error("Failed to connect to: {}", url);
      throw new RuntimeException(e);
    }
  }

  public List<DailyQuote> getHistoricalQuotesForAllStocks() {
    try {
      return downloadHistoricalQuotesForAllStocks();
    } catch (IOException | InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  private List<DailyQuote> downloadHistoricalQuotesForAllStocks()
      throws IOException, InterruptedException, ExecutionException {
    List<DailyQuote> results = Lists.newArrayList();
    List<Future> futures = Lists.newArrayList();

    ExecutorService executor = Executors.newFixedThreadPool(10);

    for (final DailyQuote dailyQuote : getDailyQuotes()) {
      Future<List<DailyQuote>> future = executor.submit(new Callable<List<DailyQuote>>() {
        @Override
        public List<DailyQuote> call() throws Exception {
          return getHistoricalQuotes(dailyQuote.getSymbol());
        }
      });
      futures.add(future);
    }

    executor.shutdown();
    executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

    for (Future<List<DailyQuote>> future : futures) {
      results.addAll(future.get());
    }

    return results;
  }

  public static void main(String args[]) throws IOException, ExecutionException, InterruptedException {
    InvestagramsService service = new InvestagramsService();
    service.getDailyQuotes();
  }
}

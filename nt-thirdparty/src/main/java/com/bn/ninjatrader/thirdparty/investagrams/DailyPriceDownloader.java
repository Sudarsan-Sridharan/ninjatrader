package com.bn.ninjatrader.thirdparty.investagrams;

import com.bn.ninjatrader.common.data.DailyQuote;
import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.thirdparty.util.DocumentDownloader;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Brad on 7/26/16.
 */
@Singleton
public class DailyPriceDownloader {

  private static final Logger log = LoggerFactory.getLogger(DailyPriceDownloader.class);
  public static final String DAILY_QUOTES_URL = "https://www.investagrams.com/Stock/RealTimeMonitoring";

  @Inject
  private DocumentDownloader documentDownloader;

  public List<DailyQuote> download() {
    try {
      Document doc = documentDownloader.getDocumentFromUrl(DAILY_QUOTES_URL);
      Element stockQuoteTable = doc.getElementById("StockQuoteTable");

      Preconditions.checkNotNull(stockQuoteTable, "Stock table not found. Perhaps site is down?");

      Elements rows = stockQuoteTable.getElementsByTag("tbody").get(0).children();
      return parseRows(rows);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private List<DailyQuote> parseRows(Elements rows) {
    List<DailyQuote> quotes = Lists.newArrayList();
    for (Element row : rows) {
      String symbol = row.attr("id");
      Elements cols = row.getElementsByTag("td");
      DailyQuote quote = parseCols(symbol, cols);
      log.debug("Parsed daily investagrams price: {}", quote);
      quotes.add(quote);
    }
    return quotes;
  }

  private DailyQuote parseCols(String symbol, Elements cols) {
    double open = NumUtil.toDouble(cols.get(5).text());
    double low = NumUtil.toDouble(cols.get(6).text());
    double high = NumUtil.toDouble(cols.get(7).text());
    double close = NumUtil.toDouble(cols.get(8).text());
    long volume = NumUtil.toLong(cols.get(9).text());
    return new DailyQuote(symbol, LocalDate.now(), open, high, low, close, volume);
  }
}

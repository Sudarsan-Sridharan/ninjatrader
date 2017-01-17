package com.bn.ninjatrader.thirdparty.investagrams;

import com.bn.ninjatrader.common.data.DailyQuote;
import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.thirdparty.exception.StockReadFailException;
import com.bn.ninjatrader.thirdparty.util.DocumentDownloader;
import com.google.common.collect.Lists;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Brad on 7/26/16.
 */
public class DownloadHistoricalPriceCallable implements Callable<List<DailyQuote>> {

  private static final Logger log = LoggerFactory.getLogger(DownloadHistoricalPriceCallable.class);
  public static final String HISTORICAL_QUOTES_URL = "https://www.investagrams.com/Stock/";
  private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd, yyyy");
  private final String symbol;
  private final DocumentDownloader documentDownloader;

  public DownloadHistoricalPriceCallable(DocumentDownloader documentDownloader, String symbol) {
    this.documentDownloader = documentDownloader;
    this.symbol = symbol;
  }

  @Override
  public List<DailyQuote> call() throws Exception {
    return getHistoricalQuotes(symbol);
  }

  private List<DailyQuote> getHistoricalQuotes(String symbol) {
    try {
      return downloadHistoricalQuotes(symbol);
    } catch (Exception e) {
      log.error("Failed to read historical quotes for symbol: {}", symbol);
      throw new StockReadFailException(symbol, e);
    }
  }

  /**
   * Connects to Investagrams, downloads the HTML document and extracts
   * the historical prices into List of DailyQuote.
   * @param symbol
   * @return
   */
  private List<DailyQuote> downloadHistoricalQuotes(String symbol) {
    Document doc = documentDownloader.getDocumentFromUrl(HISTORICAL_QUOTES_URL + symbol);

    Elements rows = doc.getElementById("HistoricalDataTable").getElementsByTag("tbody").get(0).children();

    List<DailyQuote> results = parseRows(rows);

    log.info("Found {} quotes for symbol: {}", results.size(), symbol);
    return results;
  }

  private List<DailyQuote> parseRows(Elements rows) {
    List<DailyQuote> results = Lists.newArrayList();
    for (Element row :rows) {
      Elements cols = row.getElementsByTag("td");
      DailyQuote quote = parseColumns(cols);
      results.add(quote);
    }
    return results;
  }

  private DailyQuote parseColumns(Elements cols) {
    LocalDate date = LocalDate.parse(cols.get(0).text(), dtf);
    double close = NumUtil.toDouble(cols.get(1).text());
    double open = NumUtil.toDouble(cols.get(4).text());
    double low = NumUtil.toDouble(cols.get(5).text());
    double high = NumUtil.toDouble(cols.get(6).text());
    long volume = NumUtil.toLong(cols.get(7).text());

    return new DailyQuote(symbol, date, open, high, low, close, volume);
  }
}

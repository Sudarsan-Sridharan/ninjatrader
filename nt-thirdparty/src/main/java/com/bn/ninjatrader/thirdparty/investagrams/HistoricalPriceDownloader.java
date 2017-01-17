package com.bn.ninjatrader.thirdparty.investagrams;

import com.bn.ninjatrader.common.data.DailyQuote;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.document.PriceDocument;
import com.bn.ninjatrader.thirdparty.exception.StockReadFailException;
import com.bn.ninjatrader.thirdparty.util.DocumentDownloader;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by Brad on 7/26/16.
 */
@Singleton
public class HistoricalPriceDownloader {

  private static final Logger log = LoggerFactory.getLogger(HistoricalPriceDownloader.class);

  @Inject
  private DailyPriceDownloader dailyPriceDownloader;

  @Inject
  private DocumentDownloader documentDownloader;

  @Inject
  private PriceDao priceDao;

  public List<DailyQuote> download() {
    try {
      return downloadHistoricalPriceForAllStocks();
    } catch (IOException | InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  private List<DailyQuote> downloadHistoricalPriceForAllStocks()
      throws IOException, InterruptedException, ExecutionException {
    List<DailyQuote> results = Lists.newArrayList();
    List<Future> futures = concurrentlyDownloadHistoricalPriceForAllStocks();
    List<String> failedDownloadSymbols = Lists.newArrayList();

    for (Future<List<DailyQuote>> future : futures) {
      try {
        results.addAll(future.get());
      } catch (StockReadFailException e) {
        log.error("Failed to download prices for symbol: {}", e.getSymbol());
        failedDownloadSymbols.add(e.getSymbol());
      } catch (ExecutionException e) {
        log.error("Unknown error occurred.", e);
      }
    }

    handleFailedDownloads(failedDownloadSymbols);

    return results;
  }

  private List<Future> concurrentlyDownloadHistoricalPriceForAllStocks() throws InterruptedException {
    List<Future> futures = Lists.newArrayList();
    ExecutorService executor = Executors.newFixedThreadPool(10);

//    for (final DailyQuote dailyQuote : dailyPriceDownloader.download()) {
    for (final String symbol : getSymbolsFromPrices()) {
      Callable task = new DownloadHistoricalPriceCallable(documentDownloader, symbol);
      Future<List<DailyQuote>> future = executor.submit(task);
      futures.add(future);
    }

    executor.shutdown();
    executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

    return futures;
  }

  private Collection<String> getSymbolsFromPrices() {
    List<PriceDocument> priceDocuments = priceDao.find();
    Set<String> symbols = Sets.newHashSet();
    for (PriceDocument priceDocument : priceDocuments) {
      if (priceDocument.getYear() == LocalDate.now().getYear()) {
        symbols.add(priceDocument.getSymbol());
      }
    }
    return symbols;
  }

  private void handleFailedDownloads(List<String> failedDownloadSymbols) {
    if (!failedDownloadSymbols.isEmpty()) {
      log.error("Failed to download prices for ff symbols: {}", failedDownloadSymbols);
    }
  }
}

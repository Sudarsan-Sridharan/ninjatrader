package com.bn.ninjatrader.service.event.handler;

import com.bn.ninjatrader.common.model.Algorithm;
import com.bn.ninjatrader.common.model.DailyQuote;
import com.bn.ninjatrader.messaging.listener.MessageListener;
import com.bn.ninjatrader.model.dao.AlgorithmDao;
import com.bn.ninjatrader.push.PushPublisher;
import com.bn.ninjatrader.service.annotation.cached.CachedDailyQuotes;
import com.bn.ninjatrader.service.event.message.ImportedFullPricesMessage;
import com.bn.ninjatrader.service.store.ScanResultStore;
import com.bn.ninjatrader.simulation.scanner.ScanRequest;
import com.bn.ninjatrader.simulation.scanner.ScanResult;
import com.bn.ninjatrader.simulation.scanner.StockScanner;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class ImportedFullPricesHandler implements MessageListener<ImportedFullPricesMessage> {
  private static final Logger LOG = LoggerFactory.getLogger(ImportedFullPricesHandler.class);

  private final StockScanner stockScanner;
  private final AlgorithmDao algorithmDao;
  private final ScanResultStore scanResultStore;
  private final List<DailyQuote> cachedDailyQuotes;
  private final PushPublisher pushPublisher;
  private final Clock clock;

  @Inject
  public ImportedFullPricesHandler(final StockScanner stockScanner,
                                   final AlgorithmDao algorithmDao,
                                   final PushPublisher pushPublisher,
                                   final ScanResultStore scanResultStore,
                                   final Clock clock,
                                   @CachedDailyQuotes final List<DailyQuote> cachedDailyQuotes) {
    this.stockScanner = stockScanner;
    this.algorithmDao = algorithmDao;
    this.pushPublisher = pushPublisher;
    this.scanResultStore = scanResultStore;
    this.clock = clock;
    this.cachedDailyQuotes = cachedDailyQuotes;
  }

  @Override
  public void onMessage(final ImportedFullPricesMessage message, final LocalDateTime publishTime) {
    final List<DailyQuote> quotes = message.getPayload();
    if (quotes.isEmpty()) {
      return;
    }

    final List<DailyQuote> diff = extractNewQuotes(quotes);
    if (diff.isEmpty()) {
      LOG.info("No new quotes found.");
      return;
    }

    LOG.info("Found {} new quotes.", diff.size());

    cachedDailyQuotes.clear();
    cachedDailyQuotes.addAll(quotes);

    processNewQuotes(diff);
  }

  private List<DailyQuote> extractNewQuotes(final List<DailyQuote> quotes) {
    final List<DailyQuote> diff = Lists.newArrayList(quotes);
    diff.removeAll(cachedDailyQuotes);
    return diff;
  }

  private void processNewQuotes(final List<DailyQuote> newQuotes) {
    final List<String> symbols = newQuotes.stream().map(q -> q.getSymbol()).collect(Collectors.toList());

    // Fetch all algorithms that are on realtime autoscan.
    final List<Algorithm> algorithms = algorithmDao.findAlgorithms().isAutoScan(true).now();

    LOG.info("Scanning {} symbols with {} algorithms.", newQuotes.size(), algorithms.size());

    // Scan new quotes w/ each algorithm
    algorithms.forEach(algorithm -> {
      final Map<String, ScanResult> scanResults = stockScanner
          .scan(ScanRequest.withAlgorithm(algorithm).symbols(symbols));

      pushScanResults(algorithm, scanResults);
      scanResultStore.merge(algorithm.getId(), scanResults);
    });
  }

  /**
   * Push ScanResults to clients. Only ScanResults with today's transactions will be pushed.
   * @param algorithm
   * @param scanResults
   */
  private void pushScanResults(final Algorithm algorithm, final Map<String, ScanResult> scanResults) {
    // Filter data by last transaction date. Only results w/ today's transaction will be pushed.
    final List<ScanResult> data = scanResults.values().stream()
        .filter((scanResult -> scanResult.getLastTransaction()
            .getDate().plusDays(1).isAfter(LocalDate.now(clock))))
        .sorted(Comparator.comparing(ScanResult::getSymbol))
        .collect(Collectors.toList());

    // Push data to client
    pushPublisher.push(algorithm.getUserId(), algorithm.getId(), data);
  }
}

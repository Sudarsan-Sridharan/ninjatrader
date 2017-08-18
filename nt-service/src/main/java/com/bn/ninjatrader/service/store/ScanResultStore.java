package com.bn.ninjatrader.service.store;

import com.bn.ninjatrader.service.annotation.cached.CachedScanResults;
import com.bn.ninjatrader.simulation.scanner.ScanRequest;
import com.bn.ninjatrader.simulation.scanner.ScanResult;
import com.bn.ninjatrader.simulation.scanner.StockScanner;
import com.google.common.collect.Maps;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class ScanResultStore {

  private final StockScanner stockScanner;
  private final Provider<Map<String, Map<String, ScanResult>>> resultMapProvider;

  private Map<String, Map<String, ScanResult>> resultMap;

  @Inject
  public ScanResultStore(final StockScanner stockScanner,
                         @CachedScanResults Provider<Map<String, Map<String, ScanResult>>> resultMapProvider) {
    this.stockScanner = stockScanner;
    this.resultMapProvider = resultMapProvider;
  }

  /**
   * Finds ScanResults for given algorithmId.
   * @param algorithmId
   * @return
   */
  public Optional<Map<String, ScanResult>> get(final String algorithmId) {
    checkConditions();
    return Optional.ofNullable(resultMap.get(algorithmId));
  }

  /**
   * Finds ScanResults for given algorithmId or generates one if not found in cache.
   * @param algorithmId
   * @return
   */
  public CompletableFuture<Map<String, ScanResult>> getOrCreate(final String algorithmId) {
    checkConditions();
    final Map<String, ScanResult> scanResults = resultMap.get(algorithmId);

    if (scanResults == null) {
      return CompletableFuture.supplyAsync(() -> {
          final Map<String, ScanResult> results = stockScanner.scan(ScanRequest.withAlgoId(algorithmId).allSymbols());
          resultMap.put(algorithmId, results);
          return results;
      });
    }

    return CompletableFuture.supplyAsync(() -> scanResults);
  }

  /**
   * Puts ScanResults to cache map. Existing values will be overwritten and expire time is reset.
   * @param algorithmId
   * @param scanResults
   */
  public void put(final String algorithmId, final Map<String, ScanResult> scanResults) {
    checkConditions();
    resultMap.put(algorithmId, scanResults);
  }

  /**
   * Merges contents of existing ScanResults w/ given one. Expire time is reset.
   * @param algorithmId
   * @param scanResults
   */
  public void merge(final String algorithmId, final Map<String, ScanResult> scanResults) {
    checkConditions();
    final Map<String, ScanResult> existing = get(algorithmId).orElse(Maps.newHashMap());
    existing.putAll(scanResults);
    put(algorithmId, existing);
  }

  private void checkConditions() {
    if (resultMap == null) {
      resultMap = resultMapProvider.get();
    }
  }
}

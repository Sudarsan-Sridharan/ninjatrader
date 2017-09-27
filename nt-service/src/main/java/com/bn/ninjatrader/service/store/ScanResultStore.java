package com.bn.ninjatrader.service.store;

import com.bn.ninjatrader.cache.client.api.CachedMap;
import com.bn.ninjatrader.service.annotation.cached.CachedScanResults;
import com.bn.ninjatrader.simulation.scanner.ScanResult;
import com.google.common.collect.Maps;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class ScanResultStore {

  private transient final Provider<CachedMap<String, Map<String, ScanResult>>> resultMapProvider;

  private CachedMap<String, Map<String, ScanResult>> resultMap;

  @Inject
  public ScanResultStore(@CachedScanResults Provider<CachedMap<String, Map<String, ScanResult>>> resultMapProvider) {
    this.resultMapProvider = resultMapProvider;
  }

  /**
   * Finds ScanResults for given algorithmId.
   * @param algorithmId
   * @return
   */
  public Optional<Map<String, ScanResult>> get(final String algorithmId) {
    checkState();
    return Optional.ofNullable(resultMap.get(algorithmId));
  }

  /**
   * Puts ScanResults to cache map. Existing values will be overwritten and expire time is reset.
   * @param algorithmId
   * @param scanResults
   */
  public void put(final String algorithmId, final Map<String, ScanResult> scanResults) {
    checkState();
    resultMap.put(algorithmId, scanResults, Duration.ofHours(1));
  }

  /**
   * Merges contents of existing ScanResults w/ given one. Expire time is reset.
   * @param algorithmId
   * @param scanResults
   */
  public Map<String, ScanResult> merge(final String algorithmId, final Map<String, ScanResult> scanResults) {
    checkState();
    final Map<String, ScanResult> existing = get(algorithmId).orElse(Maps.newHashMap());
    existing.putAll(scanResults);
    put(algorithmId, existing);
    return existing;
  }

  public boolean contains(final String algorithmId) {
    checkState();
    return resultMap.containsKey(algorithmId);
  }

  private void checkState() {
    if (resultMap == null) {
      resultMap = resultMapProvider.get();
    }
  }
}

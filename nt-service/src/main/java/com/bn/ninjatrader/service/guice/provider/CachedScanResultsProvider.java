package com.bn.ninjatrader.service.guice.provider;

import com.bn.ninjatrader.cache.client.CacheClient;
import com.bn.ninjatrader.simulation.scanner.ScanResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Map;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class CachedScanResultsProvider implements Provider<Map<String, Map<String, ScanResult>>> {

  private static final Logger LOG = LoggerFactory.getLogger(CachedScanResultsProvider.class);
  private static final String NAMESPACE = "scan.results";

  private final CacheClient cacheClient;

  @Inject
  public CachedScanResultsProvider(final CacheClient cacheClient) {
    this.cacheClient = cacheClient;
  }

  @Override
  public Map<String, Map<String, ScanResult>> get() {
    return cacheClient.getMap(NAMESPACE);
  }
}

package com.bn.ninjatrader.service.guice;

import com.bn.ninjatrader.cache.client.api.CacheClient;
import com.bn.ninjatrader.cache.client.api.CachedMap;
import com.bn.ninjatrader.cache.client.guice.NtCacheModule;
import com.bn.ninjatrader.common.model.DailyQuote;
import com.bn.ninjatrader.service.annotation.cached.CachedDailyQuotes;
import com.bn.ninjatrader.service.annotation.cached.CachedScanResults;
import com.bn.ninjatrader.service.cache.LazyListCache;
import com.bn.ninjatrader.service.guice.provider.CachedScanResultsProvider;
import com.bn.ninjatrader.simulation.scanner.ScanResult;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

import java.util.List;
import java.util.Map;

/**
 * @author bradwee2000@gmail.com
 */
public class NtServiceCacheModule extends NtCacheModule {

  public static final String DAILY_QUOTES_NAMESPACE = "daily.quotes";

  @Override
  protected void configure() {
    super.configure();
    bind(new TypeLiteral<CachedMap<String, Map<String, ScanResult>>>() {})
        .annotatedWith(CachedScanResults.class)
        .toProvider(CachedScanResultsProvider.class)
        .in(Singleton.class);
  }

  @Provides
  @CachedDailyQuotes
  public List<DailyQuote> provideCachedDailyQuotes(final CacheClient cacheClient) {
    return provideListCachedDailyQuotes(cacheClient);
  }

  protected List<DailyQuote> provideListCachedDailyQuotes(final CacheClient cacheClient) {
    return new LazyListCache<>(cacheClient, DAILY_QUOTES_NAMESPACE);
  }
}

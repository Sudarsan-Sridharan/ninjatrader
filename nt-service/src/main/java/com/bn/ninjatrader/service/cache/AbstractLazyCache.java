package com.bn.ninjatrader.service.cache;

import com.bn.ninjatrader.cache.client.CacheClient;

/**
 * Lazily loads cache from cache server.
 *
 * @author bradwee2000@gmail.com
 */
public abstract class AbstractLazyCache<T> {

  private final CacheClient cacheClient;
  private final String namespace;
  private T cache;

  public AbstractLazyCache(final CacheClient cacheClient, final String namespace) {
    this.cacheClient = cacheClient;
    this.namespace = namespace;
  }

  private void prepareCache() {
    if (cache == null) {
      cache = initCache(cacheClient, namespace);
    }
  }

  protected abstract T initCache(final CacheClient cacheClient, final String namespace);

  protected T getCache() {
    prepareCache();
    return cache;
  }
}

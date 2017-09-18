package com.bn.ninjatrader.cache.client.appengine;

import com.bn.ninjatrader.cache.client.api.CacheClient;
import com.bn.ninjatrader.cache.client.api.CachedMap;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class AppEngineCacheClient implements CacheClient {
  private static final Logger LOG = LoggerFactory.getLogger(AppEngineCacheClient.class);
  private static final String LISTS_NAMESPACE = "lists";

  @Override
  public void connect(final String address) {
  }

  @Override
  public void connect(final Collection<String> addresses) {
  }

  @Override
  public void shutdown() {
  }

  @Override
  public <K, V> CachedMap<K, V> getMap(final String namespace) {
    return new AppEngineCachedMap<>(MemcacheServiceFactory.getMemcacheService(namespace));
  }

  @Override
  public <T> List<T> getList(final String namespace) {
    return new AppEngineCachedList<T>(MemcacheServiceFactory.getMemcacheService(LISTS_NAMESPACE), namespace);
  }
}

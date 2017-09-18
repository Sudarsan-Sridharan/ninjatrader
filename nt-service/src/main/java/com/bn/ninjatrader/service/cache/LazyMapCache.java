package com.bn.ninjatrader.service.cache;

import com.bn.ninjatrader.cache.client.api.CacheClient;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Lazily loads map cache from cache server.
 *
 * @author bradwee2000@gmail.com
 */
public class LazyMapCache<K, V> implements Map<K, V> {

  private final CacheClient cacheClient;
  private final String namespace;
  private Map<K, V> cache;

  public LazyMapCache(final CacheClient cacheClient, final String namespace) {
    this.cacheClient = cacheClient;
    this.namespace = namespace;
  }

  private void initCache() {
    cache = cacheClient.getMap(namespace);
  }

  private void prepareCache() {
    if (cache == null) {
      initCache();
    }
  }

  @Override
  public int size() {
    prepareCache();
    return cache.size();
  }

  @Override
  public boolean isEmpty() {
    prepareCache();
    return cache.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    prepareCache();
    return cache.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    prepareCache();
    return cache.containsValue(value);
  }

  @Override
  public V get(Object key) {
    prepareCache();
    return cache.get(key);
  }

  @Override
  public V put(K key, V value) {
    prepareCache();
    return cache.put(key, value);
  }

  @Override
  public V remove(Object key) {
    prepareCache();
    return cache.remove(key);
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    prepareCache();
    cache.putAll(m);
  }

  @Override
  public void clear() {
    prepareCache();
    cache.clear();
  }

  @Override
  public Set<K> keySet() {
    prepareCache();
    return cache.keySet();
  }

  @Override
  public Collection<V> values() {
    prepareCache();
    return cache.values();
  }

  @Override
  public Set<Entry<K, V>> entrySet() {
    prepareCache();
    return cache.entrySet();
  }
}

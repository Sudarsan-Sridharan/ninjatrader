package com.bn.ninjatrader.cache.client.appengine;

import com.bn.ninjatrader.cache.client.api.CachedMap;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author bradwee2000@gmail.com
 */
public class AppEngineCachedMap<K, V> implements CachedMap<K, V> {

  private final MemcacheService memcacheService;

  public AppEngineCachedMap(final MemcacheService memcacheService) {
    this.memcacheService = memcacheService;
  }

  @Override
  public int size() {
    return 0;
  }

  @Override
  public boolean isEmpty() {
    return true;
  }

  @Override
  public boolean containsKey(Object key) {
    return memcacheService.contains(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return false;
  }

  @Override
  public V get(Object key) {
    Object value = memcacheService.get(key);
    if (value != null) {
      return (V) value;
    }
    return null;
  }

  @Override
  public Object put(Object key, Object value) {
    memcacheService.put(key, value, Expiration.byDeltaSeconds((int) Duration.ofHours(1).getSeconds()));
    return value;
  }

  @Override
  public V remove(Object key) {
    final V value = get(key);
    memcacheService.delete(key);
    return value;
  }

  @Override
  public void putAll(Map m) {
    memcacheService.putAll(m);
  }

  @Override
  public void clear() {
    memcacheService.clearAll();
  }

  @Override
  public Set keySet() {
    return Collections.emptySet();
  }

  @Override
  public Collection values() {
    return Collections.emptyList();
  }

  @Override
  public Set<Entry<K, V>> entrySet() {
    return Collections.emptySet();
  }

  @Override
  public void put(K key, V value, Duration duration) {
    memcacheService.put(key, value, Expiration.byDeltaSeconds((int) duration.getSeconds()));
  }
}

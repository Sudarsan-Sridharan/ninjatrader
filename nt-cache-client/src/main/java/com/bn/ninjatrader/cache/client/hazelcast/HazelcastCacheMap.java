package com.bn.ninjatrader.cache.client.hazelcast;

import com.bn.ninjatrader.cache.client.api.CachedMap;
import com.hazelcast.core.IMap;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author bradwee2000@gmail.com
 */
public class HazelcastCacheMap<K, V> implements CachedMap<K, V> {

  private final IMap<K, V> map;

  public HazelcastCacheMap(final IMap<K, V> map) {
    this.map = map;
  }

  @Override
  public void put(final K key, final V value, final Duration duration) {
    map.put(key, value, duration.getSeconds(), TimeUnit.SECONDS);
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return map.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return map.containsValue(value);
  }

  @Override
  public V get(Object key) {
    return map.get(key);
  }

  @Override
  public V put(K key, V value) {
    return map.put(key, value);
  }

  @Override
  public V remove(Object key) {
    return map.remove(key);
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    map.putAll(m);
  }

  @Override
  public void clear() {
    map.clear();
  }

  @Override
  public Set<K> keySet() {
    return map.keySet();
  }

  @Override
  public Collection<V> values() {
    return map.values();
  }

  @Override
  public Set<Entry<K, V>> entrySet() {
    return map.entrySet();
  }
}

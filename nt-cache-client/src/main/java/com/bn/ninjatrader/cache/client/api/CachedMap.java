package com.bn.ninjatrader.cache.client.api;

import java.time.Duration;
import java.util.Map;

/**
 * @author bradwee2000@gmail.com
 */
public interface CachedMap<K, V> extends Map<K, V> {

  void put(K key, V value, Duration duration);
}

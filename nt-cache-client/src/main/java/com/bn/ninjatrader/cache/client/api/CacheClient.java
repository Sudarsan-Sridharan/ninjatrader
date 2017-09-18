package com.bn.ninjatrader.cache.client.api;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
public interface CacheClient extends Serializable {

  /**
   * Connect to cache server using the address.
   * @param address Address of the cache server.
   */
  void connect(final String address);

  /**
   * Connect to cache server using the addresses.
   * @param addresses Addresses of the cache servers.
   */
  void connect(final Collection<String> addresses);

  /**
   * Shutdown the cache client.
   */
  void shutdown();

  /**
   * Get cached map from server.
   * @param namespace name of map
   */
  <K, V> CachedMap<K, V> getMap(final String namespace);

  /**
   * get cached list from server.
   * @param namespace name of list
   * @return
   */
  <T> List<T> getList(final String namespace);
}

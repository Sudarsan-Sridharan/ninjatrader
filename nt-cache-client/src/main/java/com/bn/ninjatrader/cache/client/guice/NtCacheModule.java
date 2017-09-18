package com.bn.ninjatrader.cache.client.guice;

import com.bn.ninjatrader.cache.client.api.CacheClient;
import com.bn.ninjatrader.cache.client.hazelcast.HazelcastCacheClient;
import com.google.inject.AbstractModule;

/**
 * @author bradwee2000@gmail.com
 */
public class NtCacheModule extends AbstractModule {

  @Override
  protected void configure() {
    bindCache();
  }

  protected void bindCache() {
    bind(CacheClient.class).to(HazelcastCacheClient.class);
  }
}

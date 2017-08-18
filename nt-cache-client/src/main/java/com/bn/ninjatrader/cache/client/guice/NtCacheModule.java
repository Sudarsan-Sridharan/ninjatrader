package com.bn.ninjatrader.cache.client.guice;

import com.bn.ninjatrader.cache.client.CacheClient;
import com.bn.ninjatrader.cache.client.HazelcastCacheClient;
import com.google.inject.AbstractModule;

/**
 * @author bradwee2000@gmail.com
 */
public class NtCacheModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(CacheClient.class).to(HazelcastCacheClient.class);
  }
}

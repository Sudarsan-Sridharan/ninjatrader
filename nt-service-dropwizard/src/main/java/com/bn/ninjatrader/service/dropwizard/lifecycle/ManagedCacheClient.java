package com.bn.ninjatrader.service.dropwizard.lifecycle;

import com.bn.ninjatrader.cache.client.CacheClient;
import io.dropwizard.lifecycle.Managed;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class ManagedCacheClient implements Managed {

  private final CacheClient cacheClient;
  private final String host;

  @Inject
  public ManagedCacheClient(final CacheClient cacheClient, final String host) {
    this.cacheClient = cacheClient;
    this.host = host;
  }

  @Override
  public void start() throws Exception {
    cacheClient.connect(host);
  }

  @Override
  public void stop() throws Exception {
    cacheClient.shutdown();
  }
}

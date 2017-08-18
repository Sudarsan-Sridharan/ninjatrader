package com.bn.ninjatrader.cache.client;

import com.google.common.collect.Lists;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class HazelcastCacheClient implements CacheClient {
  private static final Logger LOG = LoggerFactory.getLogger(HazelcastCacheClient.class);
  private static final String ERROR_MSG = "HazelcastInstance is null. Perhaps you forgot to call connect()?";
  private HazelcastInstance instance;

  @Override
  public void connect(final String address) {
    if (StringUtils.isEmpty(address)) {
      connect(Collections.emptyList());
    } else {
      connect(Lists.newArrayList(address));
    }
  }

  @Override
  public void connect(final Collection<String> addresses) {
    if (instance != null) {
      LOG.info("Already connected to cache server: {}", instance.getConfig().getNetworkConfig().getPublicAddress());
      return;
    }
    if (addresses.isEmpty()) {
      LOG.info("Connecting to local cache server.");
      instance = Hazelcast.getOrCreateHazelcastInstance(new Config().setInstanceName("dev"));
    } else {
      final ClientConfig config = new ClientConfig();
      config.getNetworkConfig().setAddresses(Lists.newArrayList(addresses));
      instance = HazelcastClient.newHazelcastClient(config);
    }
  }

  @Override
  public void shutdown() {
    if (isConnected()) {
      instance.shutdown();
    }
  }

  @Override
  public <K, V> Map<K, V> getMap(final String namespace) {
    checkNotNull(instance, ERROR_MSG);
    return instance.getMap(namespace);
  }

  @Override
  public <T> List<T> getList(final String namespace) {
    checkNotNull(instance, ERROR_MSG);
    return instance.getList(namespace);
  }

  private boolean isConnected() {
    return instance != null;
  }
}

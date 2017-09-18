package com.bn.ninjatrader.cache.client.hazelcast;

import com.hazelcast.core.IMap;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author bradwee2000@gmail.com
 */
public class HazelcastCacheMapTest {

  private HazelcastCacheMap<String, String> cacheMap;
  private IMap<String, String> delegate;

  @Before
  public void before() {
    delegate = mock(IMap.class);
    cacheMap = new HazelcastCacheMap<>(delegate);
  }

  @Test
  public void testPutWithExpiry_shouldPutToMapWithExpiry() {
    cacheMap.put("key", "value", Duration.ofHours(1));
    verify(delegate).put("key", "value", 60 * 60, TimeUnit.SECONDS);

    cacheMap.put("key2", "value2", Duration.ofMinutes(5));
    verify(delegate).put("key2", "value2", 60 * 5, TimeUnit.SECONDS);
  }
}

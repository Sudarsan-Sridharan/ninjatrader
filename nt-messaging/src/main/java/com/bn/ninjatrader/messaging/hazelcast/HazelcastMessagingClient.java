package com.bn.ninjatrader.messaging.hazelcast;

import com.bn.ninjatrader.messaging.MessagingClient;
import com.bn.ninjatrader.messaging.hazelcast.adapter.HazelcastMessageListenerAdapter;
import com.bn.ninjatrader.messaging.hazelcast.adapter.HazelcastTopicAdapter;
import com.bn.ninjatrader.messaging.listener.MessageListener;
import com.bn.ninjatrader.messaging.topic.Topic;
import com.google.common.collect.Lists;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.Collections;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class HazelcastMessagingClient implements MessagingClient {
  private static final Logger LOG = LoggerFactory.getLogger(HazelcastMessagingClient.class);
  private static final String ERROR_MSG = "HazelcastInstance is null. Perhaps you forgot to call connect()?";

  private HazelcastInstance instance;

  @Override
  public void connectLocal() {
    connect("");
  }

  @Override
  public void connect(String address) {
    if (StringUtils.isEmpty(address)) {
      connect(Collections.emptyList());
    } else {
      connect(Lists.newArrayList(address));
    }
  }

  @Override
  public void connect(Collection<String> addresses) {
    LOG.info("Connecting to messaging server: {}", addresses);
    if (instance != null) {
      LOG.info("Already connected to messaging server: {}", instance.getConfig().getNetworkConfig().getPublicAddress());
      return;
    }
    if (addresses.isEmpty()) {
      LOG.info("Connecting to local messaging server.");
      instance = Hazelcast.getOrCreateHazelcastInstance(new Config().setInstanceName("dev"));
    } else {
      final ClientConfig config = new ClientConfig();
      config.getNetworkConfig().setAddresses(Lists.newArrayList(addresses));
      instance = HazelcastClient.newHazelcastClient(config);
    }
  }

  @Override
  public void shutdown() {
    if (instance != null) {
      instance.shutdown();
    }
  }

  @Override
  public <T> Topic<T> getTopic(String topicName) {
    checkNotNull(instance, ERROR_MSG);
    final ITopic<T> topic = instance.getTopic(topicName);
    return new HazelcastTopicAdapter(topic);
  }

  @Override
  public void addSubscriber(final String topicName, final MessageListener messageListener) {
    checkNotNull(instance, ERROR_MSG);
    final ITopic topic = instance.getTopic(topicName);
    topic.addMessageListener(new HazelcastMessageListenerAdapter(messageListener));
  }
}

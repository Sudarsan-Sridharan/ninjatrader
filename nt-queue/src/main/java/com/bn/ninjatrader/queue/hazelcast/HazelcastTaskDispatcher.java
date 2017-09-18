package com.bn.ninjatrader.queue.hazelcast;

import com.bn.ninjatrader.model.util.ObjectMapperProvider;
import com.bn.ninjatrader.queue.Task;
import com.bn.ninjatrader.queue.TaskDispatcher;
import com.bn.ninjatrader.queue.config.TaskConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class HazelcastTaskDispatcher implements TaskDispatcher {
  private static final Logger LOG = LoggerFactory.getLogger(HazelcastTaskDispatcher.class);
  private static final String QUEUE_NAME = "tasks";

  private final TaskConfig taskConfig;
  private final ObjectMapper om;

  private HazelcastInstance instance;

  @Inject
  public HazelcastTaskDispatcher(final TaskConfig taskConfig, final ObjectMapperProvider objectMapperProvider) {
    this.taskConfig = taskConfig;
    this.om = objectMapperProvider.get();
  }

  @Override
  public void submitTask(final Task task) {
    checkState();
    try {
      final String json = om.writeValueAsString(task);
      instance.getQueue(QUEUE_NAME).add(json);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public void connect(final String address) {
    if (StringUtils.isEmpty(address)) {
      connect(Collections.emptyList());
    } else {
      connect(Lists.newArrayList(address));
    }
  }

  public void connect(final Collection<String> addresses) {
    if (instance != null) {
      LOG.info("Already connected to queue server: {}", instance.getConfig().getNetworkConfig().getPublicAddress());
      return;
    }
    if (addresses.isEmpty()) {
      LOG.info("Connecting to local queue server.");
      instance = Hazelcast.getOrCreateHazelcastInstance(new Config().setInstanceName("dev"));
    } else {
      final ClientConfig config = new ClientConfig();
      config.getNetworkConfig().setAddresses(Lists.newArrayList(addresses));
      instance = HazelcastClient.newHazelcastClient(config);
    }

    new Thread(new QueueConsumer(taskConfig, om, instance.getQueue(QUEUE_NAME))).start();
  }

  private void checkState() {
    if (instance == null) {
      connect("");
    }
  }

  /**
   * Queue consumer
   */
  private static final class QueueConsumer implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(QueueConsumer.class);
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";
    private final IQueue<String> queue;
    private final Client client;
    private final TaskConfig taskConfig;
    private final ObjectMapper om;

    public QueueConsumer(final TaskConfig taskConfig, final ObjectMapper om, final IQueue<String> queue) {
      this.taskConfig = taskConfig;
      this.om = om;
      this.queue = queue;
      this.client = ClientBuilder.newClient();
    }

    @Override
    public void run() {
      while (true) {
        try {
          final String json = queue.take();
          final ObjectNode objectNode = om.readValue(json, ObjectNode.class);
          final String path = objectNode.get("path").textValue();
          final JsonNode jsonNode = objectNode.get("payload");
          final String url = new URI(taskConfig.getServiceUrl() + "/" + path).normalize().toString();

          client.target(url).request()
              .header(AUTH_HEADER, BEARER + taskConfig.getServiceApiKey())
              .post(Entity.json(jsonNode.toString()));
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
  }
}

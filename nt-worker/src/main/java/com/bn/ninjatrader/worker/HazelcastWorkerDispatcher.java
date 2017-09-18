package com.bn.ninjatrader.worker;

import com.bn.ninjatrader.worker.serializer.TaskSerializer;
import com.google.common.collect.Lists;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.ExecutionCallback;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class HazelcastWorkerDispatcher implements WorkerDispatcher {
  private static final Logger LOG = LoggerFactory.getLogger(HazelcastWorkerDispatcher.class);
  private static final String ERROR_MSG = "HazelcastInstance is null. Perhaps you forgot to call connect()?";
  private static final String TASK_QUEUE_NAME = "worker.task.queue";
  private final TaskSerializer taskSerializer = new TaskSerializer();
  private final SerializerConfig callableSerializerConfig;
  private final SerializerConfig runnableSerializerConfig;

  private HazelcastInstance instance;
  private IExecutorService executorService;

  public HazelcastWorkerDispatcher() {
    callableSerializerConfig = new SerializerConfig().setImplementation(taskSerializer).setTypeClass(Callable.class);
    runnableSerializerConfig = new SerializerConfig().setImplementation(taskSerializer).setTypeClass(Runnable.class);
  }

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
      LOG.info("Already connected to worker server: {}", instance.getConfig().getNetworkConfig().getPublicAddress());
      return;
    }

    if (addresses.isEmpty()) {
      startLocalServer();
    } else {
      connectToRemoteServer(addresses);
    }
    executorService = instance.getExecutorService(TASK_QUEUE_NAME);
  }

  private void startLocalServer() {
    LOG.info("Connecting to local worker server.");
    final Config config = new Config().setInstanceName("dev");
    config.getSerializationConfig()
        .addSerializerConfig(callableSerializerConfig)
        .addSerializerConfig(runnableSerializerConfig);
    instance = Hazelcast.getOrCreateHazelcastInstance(config);
  }

  private void connectToRemoteServer(final Collection<String> addresses) {
    final ClientConfig config = new ClientConfig();
    config.getNetworkConfig().setAddresses(Lists.newArrayList(addresses));
    config.getSerializationConfig()
        .addSerializerConfig(runnableSerializerConfig)
        .addSerializerConfig(callableSerializerConfig);
    instance = HazelcastClient.newHazelcastClient(config);
  }

  @Override
  public void shutdown() {
    if (isConnected()) {
      instance.shutdown();
    }
  }

  @Override
  public <T> void submit(final Callable<T> task, final Consumer<T> onSuccess) {
    checkNotNull(instance, ERROR_MSG);
    executorService.submit(task, new ExecutionCallback<T>() {
      @Override
      public void onResponse(T response) {
        onSuccess.accept(response);
      }

      @Override
      public void onFailure(Throwable t) {
        throw new RuntimeException(t);
      }
    });
  }

  private boolean isConnected() {
    return instance != null;
  }
}

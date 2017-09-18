package com.bn.ninjatrader.worker;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class AppEngineWorkerDispatcher implements WorkerDispatcher {
  private static final Logger LOG = LoggerFactory.getLogger(AppEngineWorkerDispatcher.class);
  private static final String QUEUE_NAME = "work";

  @Override
  public void connect(final String address) {
  }

  @Override
  public void connect(final Collection<String> addresses) {

  }

  @Override
  public void shutdown() {
  }

  @Override
  public <T> void submit(final Callable<T> task, final Consumer<T> onSuccess) {
    QueueFactory.getQueue(QUEUE_NAME).add(TaskOptions.Builder.withPayload((DeferredTask) () -> {
      try {
        final T t = task.call();
        onSuccess.accept(t);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }));
  }
}

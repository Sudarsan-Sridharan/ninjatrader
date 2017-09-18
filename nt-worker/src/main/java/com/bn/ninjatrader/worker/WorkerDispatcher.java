package com.bn.ninjatrader.worker;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * Executes tasks on a different thread. It may be executed on a different machine. Tasks should be serializable.
 *
 * @author bradwee2000@gmail.com
 */
public interface WorkerDispatcher {

  void connect(final String address);

  void connect(final Collection<String> addresses);

  void shutdown();

  <T> void submit(final Callable<T> task, final Consumer<T> onSuccess);
}

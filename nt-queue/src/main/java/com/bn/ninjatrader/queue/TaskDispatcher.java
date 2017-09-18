package com.bn.ninjatrader.queue;

/**
 * @author bradwee2000@gmail.com
 */
public interface TaskDispatcher {

  /**
   * Submit task to queue
   * @param task Task containing path and payload
   */
  void submitTask(final Task task);
}

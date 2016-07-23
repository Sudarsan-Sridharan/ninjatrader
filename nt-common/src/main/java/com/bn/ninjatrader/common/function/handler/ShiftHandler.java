package com.bn.ninjatrader.common.function.handler;

/**
 * Used by ShiftForward and ShiftBackward class to handler values.
 *
 * Created by Brad on 6/1/16.
 */
public interface ShiftHandler<T> {

  /**
   * Function to set future value to past value
   * @param from
   * @param to
   */
  void process(T from, T to);

  /**
   * Function to return new instance to be added in the future of the list.
   * @return
   */
  T newInstance();

  /**
   * Function to destroy value that has been shifted to the future.
   * @param t
   */
  void destroy(T t);

}

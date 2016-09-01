package com.bn.ninjatrader.calculator.util;

import java.util.Collection;

/**
 * Created by Brad on 8/31/16.
 */
public interface CalculatingStack<T> {

  double getValue();

  void add(T t);

  void addAll(Collection<T> collection);

}

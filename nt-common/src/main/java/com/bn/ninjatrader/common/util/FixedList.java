package com.bn.ninjatrader.common.util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Brad on 5/28/16.
 */
public class FixedList<T> extends ArrayList<T> {

  public static final FixedList newInstanceWithSize(int fixedSize) {
    return new FixedList(fixedSize);
  }

  private final int maxSize;

  public FixedList(int fixedSize) {
    this.maxSize = fixedSize;
  }

  @Override
  public boolean add(T t) {
    boolean result = super.add(t);
    if (size() > maxSize) {
      remove(get(0));
    }
    return result;
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    boolean result = super.addAll(c);
    if (size() > maxSize) {
      removeRange(0, size() - maxSize);
    }
    return result;
  }

  public boolean isFull() {
    return size() == maxSize;
  }
}

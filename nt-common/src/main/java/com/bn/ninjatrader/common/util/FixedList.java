package com.bn.ninjatrader.common.util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Brad on 5/28/16.
 */
public class FixedList<T> extends ArrayList<T> {

  public static final FixedList withMaxSize(int fixedSize) {
    return new FixedList(fixedSize);
  }

  private final int maxSize;

  public FixedList(int fixedSize) {
    this.maxSize = fixedSize;
  }

  @Override
  public boolean add(T t) {
    boolean result = super.add(t);
    trimToFixedSize();
    return result;
  }

  @Override
  public void add(int index, T element) {
    super.add(index, element);
    trimToFixedSize();
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    boolean result = super.addAll(c);
    trimToFixedSize();
    return result;
  }

  @Override
  public boolean addAll(int index, Collection<? extends T> c) {
    boolean result = super.addAll(index, c);
    trimToFixedSize();
    return result;
  }

  private void trimToFixedSize() {
    if (size() > maxSize) {
      removeRange(0, size() - maxSize);
    }
  }

  public boolean isFull() {
    return size() == maxSize;
  }
}

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

  public static final FixedList withMaxSizeAndTrimDirection(int fixedSize, TrimDirection trimDirection) {
    return new FixedList(fixedSize, trimDirection);
  }

  public enum TrimDirection {
    LEFT_TO_RIGHT, RIGHT_TO_LEFT;
  }

  private final int maxSize;
  private final TrimDirection trimDirection;

  public FixedList(int fixedSize) {
    this.maxSize = fixedSize;
    this.trimDirection = TrimDirection.LEFT_TO_RIGHT;
  }

  public FixedList(int fixedSize, TrimDirection trimDirection) {
    this.maxSize = fixedSize;
    this.trimDirection = trimDirection;
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
      if (trimDirection == TrimDirection.LEFT_TO_RIGHT) {
        removeRange(0, size() - maxSize);
      } else {
        removeRange(maxSize, size());
      }
    }
  }

  public boolean isFull() {
    return size() == maxSize;
  }
}

package com.bn.ninjatrader.calculator.util;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

/**
 * Created by Brad on 7/11/16.
 */
public class FixedStack<T> {

  private final List<T> list;
  private final int fixedSize;

  public static <T> FixedStack<T> withFixedSize(int fixedSize) {
    return new FixedStack<T>(fixedSize);
  }

  public FixedStack(int fixedSize) {
    assertMinimumSize(fixedSize);

    this.list = Lists.newArrayListWithCapacity(fixedSize);
    this.fixedSize = fixedSize;
  }

  private void assertMinimumSize(int fixedSize) {
    if (fixedSize <= 0) {
      throw new IllegalArgumentException("Size must be greater than 0.");
    }
  }

  public void add(T t) {
    list.add(t);
    if (list.size() > fixedSize) {
      removeFirstIndex();
    }
    handleAfterAdd(t);
  }

  public void addAll(Collection<T> collection) {
    if (collection == null) {
      return;
    }
    for (T t : collection) {
      add(t);
    }
  }

  protected void handleAfterAdd(T added) {

  }

  private void removeFirstIndex() {
    if (list.size() == 0) {
      return;
    }
    T removed = list.remove(0);
    handleAfterRemove(removed);
  }

  protected void handleAfterRemove(T removed) {

  }

  public int size() {
    return list.size();
  }

  public T get(int index) {
    return list.get(index);
  }

  public int getFixedSize() {
    return fixedSize;
  }

  public List<T> getList() {
    return list;
  }
}

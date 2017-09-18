package com.bn.ninjatrader.common.util;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * List with fixed size. Once list is at maximum size, any new additions to the list will either not be accepted or
 * will remove the oldest entry.
 *
 * Does not implement Collection interface so Kryo can serialize this with attributes included.
 *
 * Created by Brad on 5/28/16.
 */
public class FixedList<T> implements Serializable {
  private static final Logger LOG = LoggerFactory.getLogger(FixedList.class);

  public static final FixedList withMaxSize(int fixedSize) {
    return new FixedList(fixedSize);
  }

  public static final FixedList withMaxSizeAndTrimDirection(int fixedSize, TrimDirection trimDirection) {
    return new FixedList(fixedSize, trimDirection);
  }

  public enum TrimDirection {
    LEFT_TO_RIGHT, RIGHT_TO_LEFT
  }

  private final ArrayList<T> list;
  private final int maxSize;
  private final TrimDirection trimDirection;

  private FixedList() {
    this.list = null;
    this.maxSize = 0;
    this.trimDirection = null;
  }

  public FixedList(int fixedSize) {
    this(fixedSize, TrimDirection.LEFT_TO_RIGHT);
  }

  public FixedList(int fixedSize, TrimDirection trimDirection) {
    this.list = Lists.newArrayList();
    this.maxSize = fixedSize;
    this.trimDirection = trimDirection;
  }

  public int size() {
    return list.size();
  }

  public boolean isEmpty() {
    return list.isEmpty();
  }

  public boolean contains(Object o) {
    return list.contains(o);
  }

  public Iterator<T> iterator() {
    return list.iterator();
  }

  public boolean add(T t) {
    boolean result = list.add(t);
    trimToFixedSize();
    return result;
  }

  public List<T> asList() {
    return list;
  }

  public boolean remove(Object o) {
    return list.remove(o);
  }

  public boolean containsAll(Collection<?> c) {
    return list.containsAll(c);
  }

  public void add(int index, T element) {
    list.add(index, element);
    trimToFixedSize();
  }

  public T remove(int index) {
    return list.remove(index);
  }

  public int indexOf(Object o) {
    return list.indexOf(o);
  }

  public int lastIndexOf(Object o) {
    return list.lastIndexOf(o);
  }

  public boolean addAll(Collection<? extends T> c) {
    boolean result = list.addAll(c);
    trimToFixedSize();
    return result;
  }

  public boolean removeAll(Collection<?> c) {
    return list.removeAll(c);
  }

  public void clear() {
    list.clear();
  }

  public T get(int index) {
    return list.get(index);
  }

  public T set(int index, T element) {
    return list.set(index, element);
  }

  private void trimToFixedSize() {
    if (size() > maxSize) {
      if (trimDirection == TrimDirection.LEFT_TO_RIGHT) {
        list.subList(0, size() - maxSize).clear();
      } else {
        list.subList(maxSize, size()).clear();
      }
    }
  }

  public boolean isFull() {
    return size() == maxSize;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FixedList<?> fixedList = (FixedList<?>) o;
    return maxSize == fixedList.maxSize &&
        Objects.equal(list, fixedList.list) &&
        trimDirection == fixedList.trimDirection;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(list, maxSize, trimDirection);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("maxSize", maxSize)
        .add("trimDirection", trimDirection)
        .add("list", list)
        .toString();
  }
}

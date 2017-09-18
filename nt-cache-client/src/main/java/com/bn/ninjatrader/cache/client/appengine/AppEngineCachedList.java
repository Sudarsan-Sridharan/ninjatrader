package com.bn.ninjatrader.cache.client.appengine;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author bradwee2000@gmail.com
 */
public class AppEngineCachedList<T> implements List<T> {

  private final MemcacheService memcacheService;
  private final String namespace;

  public AppEngineCachedList(final MemcacheService memcacheService, final String namespace) {
    this.memcacheService = memcacheService;
    this.namespace = namespace;
    this.memcacheService.put(this.namespace, Collections.emptyList());
  }

  private List<T> getList() {
    final Object val = this.memcacheService.get(namespace);
    if (val == null || !(val instanceof List)) {
      return Collections.emptyList();
    }
    return Lists.newArrayList((List) val);
  }

  @Override
  public int size() {
    return getList().size();
  }

  @Override
  public boolean isEmpty() {
    return getList().isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return getList().contains(o);
  }

  @Override
  public Iterator<T> iterator() {
    return getList().iterator();
  }

  @Override
  public Object[] toArray() {
    return getList().toArray();
  }

  @Override
  public <T1> T1[] toArray(T1[] a) {
    return getList().toArray(a);
  }

  @Override
  public boolean add(T t) {
    final List list = getList();
    final boolean result = list.add(t);
    memcacheService.put(namespace, list);
    return result;
  }

  @Override
  public boolean remove(Object o) {
    final List list = getList();
    final boolean result = list.remove(o);
    memcacheService.put(namespace, list);
    return result;
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return getList().containsAll(c);
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    final List list = getList();
    final boolean result = list.addAll(c);
    memcacheService.put(namespace, list);
    return result;
  }

  @Override
  public boolean addAll(int index, Collection<? extends T> c) {
    final List list = getList();
    final boolean result = list.addAll(index, c);
    memcacheService.put(namespace, list);
    return result;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    final List list = getList();
    final boolean result = list.removeAll(c);
    memcacheService.put(namespace, list);
    return result;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    final List list = getList();
    final boolean result = list.retainAll(c);
    memcacheService.put(namespace, list);
    return result;
  }

  @Override
  public void clear() {
    memcacheService.put(namespace, Collections.emptyList());
  }

  @Override
  public T get(int index) {
    return null;
  }

  @Override
  public T set(int index, T element) {
    return null;
  }

  @Override
  public void add(int index, T element) {

  }

  @Override
  public T remove(int index) {
    return null;
  }

  @Override
  public int indexOf(Object o) {
    return 0;
  }

  @Override
  public int lastIndexOf(Object o) {
    return 0;
  }

  @Override
  public ListIterator<T> listIterator() {
    return null;
  }

  @Override
  public ListIterator<T> listIterator(int index) {
    return null;
  }

  @Override
  public List<T> subList(int fromIndex, int toIndex) {
    return null;
  }
}

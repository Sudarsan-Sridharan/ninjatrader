package com.bn.ninjatrader.service.cache;

import com.bn.ninjatrader.cache.client.CacheClient;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 ** Lazily loads list cache from cache server.
 *
 * @author bradwee2000@gmail.com
 */
public class LazyListCache<T> extends AbstractLazyCache<List<T>> implements List<T> {

  public LazyListCache(final CacheClient cacheClient, final String namespace) {
    super(cacheClient, namespace);
  }

  @Override
  protected List<T> initCache(final CacheClient cacheClient, final String namespace) {
    return cacheClient.getList(namespace);
  }

  @Override
  public int size() {
    return getCache().size();
  }

  @Override
  public boolean isEmpty() {
    return getCache().isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return getCache().contains(o);
  }

  @Override
  public Iterator<T> iterator() {
    return getCache().iterator();
  }

  @Override
  public Object[] toArray() {
    return getCache().toArray();
  }

  @Override
  public <T1> T1[] toArray(T1[] a) {
    return getCache().toArray(a);
  }

  @Override
  public boolean add(T t) {
    return getCache().add(t);
  }

  @Override
  public boolean remove(Object o) {
    return getCache().remove(o);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return getCache().containsAll(c);
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    return getCache().addAll(c);
  }

  @Override
  public boolean addAll(int index, Collection<? extends T> c) {
    return getCache().addAll(index, c);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return getCache().removeAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return getCache().retainAll(c);
  }

  @Override
  public void clear() {
    getCache().clear();
  }

  @Override
  public T get(int index) {
    return getCache().get(index);
  }

  @Override
  public T set(int index, T element) {
    return getCache().set(index, element);
  }

  @Override
  public void add(int index, T element) {
    getCache().add(index, element);
  }

  @Override
  public T remove(int index) {
    return getCache().remove(index);
  }

  @Override
  public int indexOf(Object o) {
    return getCache().indexOf(o);
  }

  @Override
  public int lastIndexOf(Object o) {
    return getCache().lastIndexOf(o);
  }

  @Override
  public ListIterator<T> listIterator() {
    return getCache().listIterator();
  }

  @Override
  public ListIterator<T> listIterator(int index) {
    return getCache().listIterator(index);
  }

  @Override
  public List<T> subList(int fromIndex, int toIndex) {
    return getCache().subList(fromIndex, toIndex);
  }
}

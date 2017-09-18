package com.bn.ninjatrader.queue;

import com.google.common.base.MoreObjects;

import java.io.Serializable;

/**
 * @author bradwee2000@gmail.com
 */
public class Task<T> implements Serializable {

  public static final <T> Task withPath(final String path) {
      return new Task<T>(path);
  }

  private String path;
  private T payload;

  private Task() {}

  private Task(final String path) {
    this.path = path;
  }

  public Task payload(final T payload) {
    this.payload = payload;
    return this;
  }

  public T getPayload() {
    return payload;
  }

  public String getPath() {
    return path;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("path", path)
        .add("payload", payload)
        .toString();
  }
}

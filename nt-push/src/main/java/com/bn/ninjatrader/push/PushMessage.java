package com.bn.ninjatrader.push;

/**
 * @author bradwee2000@gmail.com
 */
public interface PushMessage<T> {

  String getName();

  T getData();
}

package com.bn.ninjatrader.common.util;

import com.google.common.base.Preconditions;

import java.util.List;

/**
 * Created by Brad on 8/18/16.
 */
public class ListUtil {

  private ListUtil() {}

  public static <T> void fillToSize(List<T> list, T filler, long size) {
    Preconditions.checkNotNull(list);

    for (int i = list.size(); i < size; i++) {
      list.add(0, filler);
    }
  }
}

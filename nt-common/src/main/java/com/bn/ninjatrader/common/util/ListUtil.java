package com.bn.ninjatrader.common.util;

import com.google.common.base.Preconditions;

import java.util.List;

/**
 * Created by Brad on 8/18/16.
 */
public class ListUtil {

  private ListUtil() {}

  public static <T> void fillToSize(List<T> dataList, T filler, long size) {
    Preconditions.checkNotNull(dataList);

    for (int i = dataList.size(); i < size; i++) {
      dataList.add(0, filler);
    }
  }
}

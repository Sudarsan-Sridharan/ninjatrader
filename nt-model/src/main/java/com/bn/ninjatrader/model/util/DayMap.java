package com.bn.ninjatrader.model.util;

import java.util.HashMap;

/**
 * Created by Brad on 5/2/16.
 */
public class DayMap<T> extends HashMap<Integer, T> {

  public static <T> DayMap<T> newDayMap() {
    return new DayMap<T>();
  }

  public T day(Integer day) {
    return this.get(day);
  }
}

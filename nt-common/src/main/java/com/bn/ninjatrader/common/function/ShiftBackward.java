package com.bn.ninjatrader.common.function;

import java.util.List;

/**
 * Created by Brad on 6/1/16.
 */
public class ShiftBackward<T> extends AbstractShiftProcess<T> {

  private ShiftBackward() {}

  public static <T> ShiftBackward<T> forValues(List<T> values) {
    ShiftBackward<T> shiftBackward = new ShiftBackward<>();
    shiftBackward.values(values);
    return shiftBackward;
  }
}

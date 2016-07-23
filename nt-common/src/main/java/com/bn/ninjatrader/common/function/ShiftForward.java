package com.bn.ninjatrader.common.function;

import java.util.Collections;
import java.util.List;

/**
 * Created by Brad on 6/1/16.
 */
public class ShiftForward<T> extends AbstractShiftProcess<T> {

  public static <T> ShiftForward<T> forValues(List<T> values) {
    ShiftForward<T> shiftForward = new ShiftForward<>();
    shiftForward.values(values);
    return shiftForward;
  }

  private ShiftForward() {}

  @Override
  public void execute() {

    addFutures();

    // Reverse the order so past doesn't overwrite the future value
    Collections.reverse(getValues());

    super.execute();

    // Reverse the order again to normalize.
    Collections.reverse(getValues());
  }

  private void addFutures() {
    for (int i = 0; i < getPeriod(); i++) {
      getValues().add(getHandler().newInstance());
    }
  }
}

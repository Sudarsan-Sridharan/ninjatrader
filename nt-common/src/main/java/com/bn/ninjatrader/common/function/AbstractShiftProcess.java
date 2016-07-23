package com.bn.ninjatrader.common.function;

import com.bn.ninjatrader.common.function.handler.ShiftHandler;
import com.bn.ninjatrader.common.util.FixedList;

import java.util.List;

/**
 * Created by Brad on 6/1/16.
 */
public abstract class AbstractShiftProcess<T> {

  private int period;
  private ShiftHandler<T> handler;
  private List<T> values;

  public AbstractShiftProcess values(List<T> values) {
    this.values = values;
    return this;
  }

  public AbstractShiftProcess period(int period) {
    this.period = period;
    return this;
  }

  public AbstractShiftProcess handler(ShiftHandler<T> handler) {
    this.handler = handler;
    return this;
  }

  public void execute() {
    if (period == 0) {
      return;
    }

    List<T> queue = FixedList.newInstanceWithSize(period);

    int count = 0;
    for (T t : values) {
      if (count >= period) {
        handler.process(t, queue.get(0));
      }
      queue.add(t);
      count++;
    }

    // Destroy shifted
    for (T t : queue) {
      handler.destroy(t);
    }
  }

  protected int getPeriod() {
    return period;
  }

  protected List<T> getValues() {
    return values;
  }

  protected ShiftHandler<T> getHandler() {
    return handler;
  }
}

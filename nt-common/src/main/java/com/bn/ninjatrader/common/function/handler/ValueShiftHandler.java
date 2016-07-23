package com.bn.ninjatrader.common.function.handler;

import com.bn.ninjatrader.common.data.Value;
import com.google.inject.Singleton;

/**
 * Created by Brad on 6/1/16.
 */
@Singleton
public class ValueShiftHandler implements ShiftHandler<Value> {

  public void process(Value from, Value to) {
    to.setValue(from.getValue());
  }

  public Value newInstance() {
    return new Value();
  }

  public void destroy(Value value) {
    value.setValue(0);
  }
}

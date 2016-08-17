package com.bn.ninjatrader.testplay.type;

import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.testplay.operation.Operation;
import com.bn.ninjatrader.testplay.parameter.BarParameters;

/**
 * Created by Brad on 8/2/16.
 */
public enum Operator {

  PLUS {
    @Override
    public double exec(Operation lhs, Operation rhs, BarParameters barParameters) {
      return NumUtil.plus(lhs.getValue(barParameters), rhs.getValue(barParameters));
    }
  },
  MINUS {
    @Override
    public double exec(Operation lhs, Operation rhs, BarParameters barParameters) {
      return NumUtil.minus(lhs.getValue(barParameters), rhs.getValue(barParameters));
    }
  },
  MULTIPLY {
    @Override
    public double exec(Operation lhs, Operation rhs, BarParameters barParameters) {
      return NumUtil.multiply(lhs.getValue(barParameters), rhs.getValue(barParameters));
    }
  },
  DIVIDE {
    @Override
    public double exec(Operation lhs, Operation rhs, BarParameters barParameters) {
      return NumUtil.divide(lhs.getValue(barParameters), rhs.getValue(barParameters));
    }
  };

  public abstract double exec(Operation lhs, Operation rhs, BarParameters barParameters);
}

package com.bn.ninjatrader.simulation.type;

import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.operation.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Brad on 8/2/16.
 */
public enum Operator {
  PLUS {
    @Override
    public double exec(final Operation lhs, final Operation rhs, final BarData barData) {
      return NumUtil.plus(lhs.getValue(barData), rhs.getValue(barData));
    }
  },
  MINUS {
    @Override
    public double exec(final Operation lhs, final Operation rhs, final BarData barData) {
      return NumUtil.minus(lhs.getValue(barData), rhs.getValue(barData));
    }
  },
  MULTIPLY {
    @Override
    public double exec(final Operation lhs, final Operation rhs, final BarData barData) {
      return NumUtil.multiply(lhs.getValue(barData), rhs.getValue(barData));
    }
  },
  DIVIDE {
    @Override
    public double exec(final Operation lhs, final Operation rhs, final BarData barData) {
      return NumUtil.divide(lhs.getValue(barData), rhs.getValue(barData));
    }
  };

  private static final Logger log = LoggerFactory.getLogger(Operator.class);

  public abstract double exec(final Operation lhs, final Operation rhs, final BarData barData);
}

package com.bn.ninjatrader.logical.expression.operator;

import com.bn.ninjatrader.common.util.NumUtil;
import com.bn.ninjatrader.logical.expression.model.Data;
import com.bn.ninjatrader.logical.expression.operation.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Brad on 8/2/16.
 */
public enum Operator {
  PLUS {
    @Override
    public double exec(final Operation lhs, final Operation rhs, final Data data) {
      return NumUtil.plus(lhs.getValue(data), rhs.getValue(data));
    }
  },
  MINUS {
    @Override
    public double exec(final Operation lhs, final Operation rhs, final Data data) {
      return NumUtil.minus(lhs.getValue(data), rhs.getValue(data));
    }
  },
  MULTIPLY {
    @Override
    public double exec(final Operation lhs, final Operation rhs, final Data data) {
      return NumUtil.multiply(lhs.getValue(data), rhs.getValue(data));
    }
  },
  DIVIDE {
    @Override
    public double exec(final Operation lhs, final Operation rhs, final Data data) {
      return NumUtil.divide(lhs.getValue(data), rhs.getValue(data));
    }
  };

  private static final Logger log = LoggerFactory.getLogger(Operator.class);

  public abstract double exec(final Operation lhs, final Operation rhs, final Data data);
}

package com.bn.ninjatrader.logical.expression.operator;

/**
 * Created by Brad on 8/2/16.
 */
public enum InequalityOperator {
  EQ {
    @Override
    public boolean isMatch(double lhsValue, double rhsValue) {
      return lhsValue == rhsValue;
    }
  },

  GT {
    @Override
    public boolean isMatch(double lhsValue, double rhsValue) {
      return lhsValue > rhsValue;
    }
  },

  GTE {
    @Override
    public boolean isMatch(double lhsValue, double rhsValue) {
      return lhsValue >= rhsValue;
    }
  },

  LT {
    @Override
    public boolean isMatch(double lhsValue, double rhsValue) {
      return lhsValue < rhsValue;
    }
  },

  LTE {
    @Override
    public boolean isMatch(double lhsValue, double rhsValue) {
      return lhsValue <= rhsValue;
    }
  };

  public abstract boolean isMatch(double lhsValue, double rhsValue);
}

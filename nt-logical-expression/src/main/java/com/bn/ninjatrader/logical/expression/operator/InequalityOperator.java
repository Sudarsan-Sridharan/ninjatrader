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

    @Override
    public String toString() {
      return "=";
    }
  },

  GT {
    @Override
    public boolean isMatch(double lhsValue, double rhsValue) {
      return lhsValue > rhsValue;
    }

    @Override
    public String toString() {
      return ">";
    }
  },

  GTE {
    @Override
    public boolean isMatch(double lhsValue, double rhsValue) {
      return lhsValue >= rhsValue;
    }

    @Override
    public String toString() {
      return ">=";
    }
  },

  LT {
    @Override
    public boolean isMatch(double lhsValue, double rhsValue) {
      return lhsValue < rhsValue;
    }

    @Override
    public String toString() {
      return "<";
    }
  },

  LTE {
    @Override
    public boolean isMatch(double lhsValue, double rhsValue) {
      return lhsValue <= rhsValue;
    }

    @Override
    public String toString() {
      return "<=";
    }
  };

  public abstract boolean isMatch(double lhsValue, double rhsValue);
}

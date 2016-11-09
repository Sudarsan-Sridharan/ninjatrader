package com.bn.ninjatrader.simulation.type;

/**
 * Created by Brad on 8/2/16.
 */
public enum InequalityOperator {
  EQUALS {
    @Override
    public boolean isMatch(double lhsValue, double rhsValue) {
      return lhsValue == rhsValue;
    }
  },

  GREATER_THAN {
    @Override
    public boolean isMatch(double lhsValue, double rhsValue) {
      return lhsValue > rhsValue;
    }
  },

  GREATER_THAN_EQUALS {
    @Override
    public boolean isMatch(double lhsValue, double rhsValue) {
      return lhsValue >= rhsValue;
    }
  },

  LESS_THAN {
    @Override
    public boolean isMatch(double lhsValue, double rhsValue) {
      return lhsValue < rhsValue;
    }
  },

  LESS_THAN_OR_EQUALS {
    @Override
    public boolean isMatch(double lhsValue, double rhsValue) {
      return lhsValue <= rhsValue;
    }
  };

  public abstract boolean isMatch(double lhsValue, double rhsValue);
}

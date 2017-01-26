package com.bn.ninjatrader.logical.expression.condition;

import com.bn.ninjatrader.logical.expression.model.Data;
import com.bn.ninjatrader.logical.expression.operation.Variable;

/**
 * @author bradwee2000@gmail.com
 */
public class MockData implements Data {
  @Override
  public Double get(Variable variable) {
    return Double.MAX_VALUE;
  }
}

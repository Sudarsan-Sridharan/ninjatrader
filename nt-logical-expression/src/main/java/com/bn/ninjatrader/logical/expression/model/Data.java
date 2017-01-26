package com.bn.ninjatrader.logical.expression.model;

import com.bn.ninjatrader.logical.expression.operation.Variable;

/**
 * @author bradwee2000@gmail.com
 */
public interface Data {

  Double get(final Variable variable);
}

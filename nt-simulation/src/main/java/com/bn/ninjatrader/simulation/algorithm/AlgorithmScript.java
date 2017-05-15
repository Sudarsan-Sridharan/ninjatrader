package com.bn.ninjatrader.simulation.algorithm;

import com.bn.ninjatrader.logical.expression.operation.Variable;

import java.util.Collection;

/**
 * @author bradwee2000@gmail.com
 */
public interface AlgorithmScript {

  ScriptRunner newRunner();

  Collection<Variable> getVariables();
}

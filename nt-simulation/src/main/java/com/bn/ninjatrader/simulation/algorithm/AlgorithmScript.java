package com.bn.ninjatrader.simulation.algorithm;

import com.bn.ninjatrader.simulation.logic.Variable;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author bradwee2000@gmail.com
 */
public interface AlgorithmScript extends Serializable {

  ScriptRunner newRunner();

  Collection<Variable> getVariables();
}

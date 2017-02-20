package com.bn.ninjatrader.simulation.calculator;

import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.annotation.VarCalculatorMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
public class VarCalculatorFactory {
  private static final Logger LOG = LoggerFactory.getLogger(VarCalculatorFactory.class);
  private final Map<String, Class> varCalculatorMap;

  @Inject
  public VarCalculatorFactory(@VarCalculatorMap final Map<String, Class> varCalculatorMap) {
    this.varCalculatorMap = varCalculatorMap;
  }

  public VarCalculator createForVariable(final Variable variable) {
    try {
      if (varCalculatorMap.containsKey(variable.getDataType())) {
        return (VarCalculator) varCalculatorMap.get(variable.getDataType())
            .getConstructor(int.class)
            .newInstance(variable.getPeriod());
      } else {
        throw new IllegalArgumentException(String.format("Variable [%s] not supported.", variable));
      }
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  public Set<VarCalculator> createForVariables(final Collection<Variable> variables) {
    final Set<VarCalculator> set = new HashSet<>();
    for (final Variable variable : variables) {
      set.add(createForVariable(variable));
    }
    return set;
  }
}

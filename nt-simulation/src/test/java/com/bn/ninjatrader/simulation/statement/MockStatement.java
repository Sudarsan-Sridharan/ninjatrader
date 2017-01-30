package com.bn.ninjatrader.simulation.statement;

import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.data.BarData;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author bradwee2000@gmail.com
 */
public class MockStatement implements Statement {

  public static final MockStatement newInstance() {
    return new MockStatement();
  }

  private int runCount = 0;
  private Set<Variable> variables = Sets.newHashSet();

  @Override
  public void run(final BarData barData) {
    runCount++;
  }

  public int getRunCount() {
    return runCount;
  }

  public boolean isRun() {
    return runCount > 0;
  }

  public void reset() {
    runCount = 0;
  }

  public MockStatement withVariables(final Variable variable, final Variable ... more) {
    variables.clear();
    variables.addAll(Lists.asList(variable, more));
    return this;
  }

  @Override
  public Set<Variable> getVariables() {
    return Sets.newHashSet(variables);
  }
}

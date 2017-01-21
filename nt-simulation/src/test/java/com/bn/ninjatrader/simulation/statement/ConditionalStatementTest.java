package com.bn.ninjatrader.simulation.statement;

import com.bn.ninjatrader.simulation.condition.FalseCondition;
import com.bn.ninjatrader.simulation.condition.TrueCondition;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.World;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author bradwee2000@gmail.com
 */
public class ConditionalStatementTest {

  private final ConditionalStatment trueStatement = ConditionalStatment.builder()
      .condition(TrueCondition.instance())
      .then(EmptyStatement.instance()).otherwise(EmptyStatement.instance()).build();

  private World world;
  private BarData barData;
  private Statement thenStatement;
  private Statement elseStatement;

  @Before
  public void before() {
    world = mock(World.class);
    barData = mock(BarData.class);
    thenStatement = mock(Statement.class);
    elseStatement = mock(Statement.class);
  }

  @Test
  public void testRunWithTrueCondition_shouldRunThenStatement() {
    final ConditionalStatment trueStatement = ConditionalStatment.builder()
        .condition(TrueCondition.instance())
        .then(thenStatement).otherwise(elseStatement).build();

    trueStatement.run(world, barData);

    verify(thenStatement).run(world, barData);
    verify(elseStatement, times(0)).run(world, barData);
  }

  @Test
  public void testRunWithFalseCondition_shouldRunElseStatement() {
    final ConditionalStatment trueStatement = ConditionalStatment.builder()
        .condition(FalseCondition.instance())
        .then(thenStatement).otherwise(elseStatement).build();

    trueStatement.run(world, barData);

    verify(elseStatement).run(world, barData);
    verify(thenStatement, times(0)).run(world, barData);
  }
}

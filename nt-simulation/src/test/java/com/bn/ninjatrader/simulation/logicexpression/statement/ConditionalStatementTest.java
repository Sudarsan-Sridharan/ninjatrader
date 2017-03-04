package com.bn.ninjatrader.simulation.logicexpression.statement;

import com.bn.ninjatrader.logical.expression.condition.FalseCondition;
import com.bn.ninjatrader.logical.expression.condition.TrueCondition;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.logicexpression.statement.ConditionalStatement;
import com.bn.ninjatrader.simulation.logicexpression.statement.Statement;
import com.bn.ninjatrader.simulation.model.World;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * @author bradwee2000@gmail.com
 */
public class ConditionalStatementTest {

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
    final ConditionalStatement trueStatement = ConditionalStatement.builder()
        .condition(TrueCondition.instance())
        .then(thenStatement).otherwise(elseStatement).build();

    trueStatement.run(barData);

    verify(thenStatement).run(barData);
    verify(elseStatement, times(0)).run(barData);
  }

  @Test
  public void testRunWithFalseCondition_shouldRunElseStatement() {
    final ConditionalStatement trueStatement = ConditionalStatement.builder()
        .condition(FalseCondition.instance())
        .then(thenStatement).otherwise(elseStatement).build();

    trueStatement.run(barData);

    verify(elseStatement).run(barData);
    verify(thenStatement, times(0)).run(barData);
  }
}
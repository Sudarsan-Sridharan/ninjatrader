package com.bn.ninjatrader.testplay.operation.function;

import com.bn.ninjatrader.testplay.simulation.data.BarData;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 8/29/16.
 */
public class HighestInLastNBarsFunctionTest extends AbstractHistoryFunctionTest {

  @Test
  public void testWithSingleHistoryBar() {
    barDataFactory.create(price1);
    BarData barData = barDataFactory.create(price1);

    HighestInLastNBarsFunction function = new HighestInLastNBarsFunction(priceClose, 0);
    assertEquals(function.getValue(barData), 0.0);

    function = new HighestInLastNBarsFunction(priceClose, 1);
    assertEquals(function.getValue(barData), 1.4);

    function = new HighestInLastNBarsFunction(priceClose, 100);
    assertEquals(function.getValue(barData), 1.4);
  }

  @Test
  public void testWithMultipleBars() {
    barDataFactory.create(price1);
    BarData barData = barDataFactory.create(price2);

    HighestInLastNBarsFunction function = new HighestInLastNBarsFunction(priceClose, 1);
    assertEquals(function.getValue(barData), 1.4);

    barData = barDataFactory.create(price3);

    function = new HighestInLastNBarsFunction(priceClose, 1);
    assertEquals(function.getValue(barData), 2.4);

    function = new HighestInLastNBarsFunction(priceClose, 2);
    assertEquals(function.getValue(barData), 2.4);

    function = new HighestInLastNBarsFunction(priceOpen, 2);
    assertEquals(function.getValue(barData), 2.1);
  }
}

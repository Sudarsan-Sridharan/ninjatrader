package com.bn.ninjatrader.simulation.operation.function;

import com.bn.ninjatrader.simulation.data.BarData;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 8/29/16.
 */
public class HistoryFunctionTest extends AbstractHistoryFunctionTest {

  @Test
  public void testSingleHistoryFunction() {
    BarData barData = barDataFactory.create(price1);

    HistoryFunction function = new HistoryFunction(priceClose, 0);
    assertEquals(function.getValue(barData), 1.4);

    function = new HistoryFunction(priceClose, 1);
    assertEquals(function.getValue(barData), 0.0);
  }

  @Test
  public void testMultiHistoryFunction() {
    barDataFactory.create(price1);
    barDataFactory.create(price2);
    BarData barData = barDataFactory.create(price3);

    HistoryFunction function = new HistoryFunction(priceClose, 0);
    assertEquals(function.getValue(barData), 3.4);

    function = new HistoryFunction(priceClose, 1);
    assertEquals(function.getValue(barData), 2.4);

    function = new HistoryFunction(priceClose, 2);
    assertEquals(function.getValue(barData), 1.4);

    function = new HistoryFunction(priceClose, 100);
    assertEquals(function.getValue(barData), 0.0);
  }
}

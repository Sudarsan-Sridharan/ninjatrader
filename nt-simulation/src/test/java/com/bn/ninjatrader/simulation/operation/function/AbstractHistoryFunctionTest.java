package com.bn.ninjatrader.simulation.operation.function;

import com.bn.ninjatrader.simulation.data.DataType;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.simulation.operation.Operation;
import com.bn.ninjatrader.simulation.operation.UnaryOperation;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.data.BarDataFactory;
import org.testng.annotations.BeforeMethod;

import static com.bn.ninjatrader.simulation.data.DataType.PRICE_CLOSE;
import static com.bn.ninjatrader.simulation.data.DataType.PRICE_OPEN;

/**
 * Created by Brad on 8/30/16.
 */
public class AbstractHistoryFunctionTest {
  BarDataFactory barDataFactory;

  final Price price1 = new Price(null, 1.1, 1.2, 1.3, 1.4, 10000);
  final Price price2 = new Price(null, 2.1, 2.2, 2.3, 2.4, 20000);
  final Price price3 = new Price(null, 3.1, 3.2, 3.3, 3.4, 30000);

  final Operation priceClose = UnaryOperation.of(PRICE_CLOSE);
  final Operation priceOpen = UnaryOperation.of(PRICE_OPEN);

  BarData barData1;
  BarData barData2;
  BarData barData3;

  @BeforeMethod
  public void setup() {
    barDataFactory = new BarDataFactory();

    barData1 = BarData.forPrice(price1);
    barData1.put(DataType.PRICE_CLOSE, 1.4);

    barData2 = BarData.forPrice(price2);
    barData2.put(DataType.PRICE_CLOSE, 2.4);

    barData3 = BarData.forPrice(price3);
    barData3.put(DataType.PRICE_CLOSE, 3.4);
  }
}

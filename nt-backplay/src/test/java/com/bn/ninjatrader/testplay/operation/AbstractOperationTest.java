package com.bn.ninjatrader.testplay.operation;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.testplay.simulation.data.BarData;
import org.testng.annotations.BeforeClass;

/**
 * Created by Brad on 8/6/16.
 */
public class AbstractOperationTest {

  protected BarData barData;

  @BeforeClass
  public void setup() {
    Price price = new Price();
    price.setOpen(1.0);
    price.setHigh(2.0);
    price.setLow(3.0);
    price.setClose(4.0);
    price.setVolume(10000);

    barData = new BarData();
    barData.put(price);
  }
}

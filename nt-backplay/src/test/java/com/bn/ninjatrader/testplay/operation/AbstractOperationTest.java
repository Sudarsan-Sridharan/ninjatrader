package com.bn.ninjatrader.testplay.operation;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.testplay.parameter.BarParameters;
import org.testng.annotations.BeforeClass;

/**
 * Created by a- on 8/6/16.
 */
public class AbstractOperationTest {

  protected BarParameters barParameters;

  @BeforeClass
  public void setup() {
    Price price = new Price();
    price.setOpen(1.0);
    price.setHigh(2.0);
    price.setLow(3.0);
    price.setClose(4.0);
    price.setVolume(10000);

    barParameters = new BarParameters();
    barParameters.put(price);
  }
}

package com.bn.ninjatrader.testplay.operation;

import com.bn.ninjatrader.testplay.simulation.data.DataType;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.testplay.simulation.data.BarData;
import com.bn.ninjatrader.testplay.simulation.data.DataMap;
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

    DataMap dataMap = new DataMap();
    dataMap.put(DataType.PRICE_OPEN, 1.0);
    dataMap.put(DataType.PRICE_HIGH, 2.0);
    dataMap.put(DataType.PRICE_LOW, 3.0);
    dataMap.put(DataType.PRICE_CLOSE, 4.0);
    dataMap.put(DataType.VOLUME, 10000);

    barData = BarData.forPrice(price);
    barData.put(dataMap);
  }
}

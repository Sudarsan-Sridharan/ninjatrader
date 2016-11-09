package com.bn.ninjatrader.simulation.adaptor;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.simulation.data.DataMap;
import com.bn.ninjatrader.simulation.data.DataType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 8/18/16.
 */
public class PriceDataMapAdaptorTest {

  private PriceDataMapAdaptor adaptor;

  @BeforeClass
  public void setup() {
    adaptor = new PriceDataMapAdaptor();
  }

  @Test
  public void testToDataMap() {
    Price price = TestUtil.randomPrice();
    price.setVolume(1000000000000l);
    DataMap dataMap = adaptor.toDataMap(price);

    assertEquals(dataMap.get(DataType.PRICE_OPEN), price.getOpen());
    assertEquals(dataMap.get(DataType.PRICE_HIGH), price.getHigh());
    assertEquals(dataMap.get(DataType.PRICE_LOW), price.getLow());
    assertEquals(dataMap.get(DataType.PRICE_CLOSE), price.getClose());
    assertEquals(dataMap.get(DataType.VOLUME).longValue(), price.getVolume());
  }
}

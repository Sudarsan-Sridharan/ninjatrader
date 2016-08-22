package com.bn.ninjatrader.testplay.simulation.data;

import com.bn.ninjatrader.common.data.Price;
import org.testng.annotations.Test;

import static com.bn.ninjatrader.common.data.DataType.*;
import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 8/11/16.
 */
public class BarDataTest {

  @Test
  public void testPutPrice() {
    Price price = new Price(10, 20, 30, 40, 10000);

    BarData params = new BarData().put(price);

    assertValidPriceParams(params, price);
  }

  @Test
  public void testPutDataMap() {
    DataMap dataMap = new DataMap();
    dataMap.put(CHIKOU, 100d);
    dataMap.put(TENKAN, 200d);

    BarData params = new BarData().put(dataMap);

    assertEquals(params.get(CHIKOU), 100d);
    assertEquals(params.get(TENKAN), 200d);
  }

  @Test
  public void testOverwriteDataMap() {
    DataMap dataMap = new DataMap();
    dataMap.put(CHIKOU, 100d);
    dataMap.put(TENKAN, 200d);

    BarData params = new BarData().put(dataMap);

    DataMap overlayDataMap = new DataMap();
    overlayDataMap.put(TENKAN, 300d);
    overlayDataMap.put(KIJUN, 400d);

    params.put(overlayDataMap);

    assertEquals(params.get(CHIKOU), 100d);
    assertEquals(params.get(TENKAN), 300d);
    assertEquals(params.get(KIJUN), 400d);
  }

  private void assertValidPriceParams(BarData params, Price price) {
    assertEquals(params.get(PRICE_OPEN), price.getOpen());
    assertEquals(params.get(PRICE_HIGH), price.getHigh());
    assertEquals(params.get(PRICE_LOW), price.getLow());
    assertEquals(params.get(PRICE_CLOSE), price.getClose());
    assertEquals(params.get(VOLUME).longValue(), price.getVolume());
    assertEquals(params.getPrice(), price);
  }
}

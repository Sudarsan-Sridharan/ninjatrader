package com.bn.ninjatrader.testplay.parameter;

import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.testplay.type.DataType;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 8/11/16.
 */
public class BarParameterTest {

  @Test
  public void testGetPrice() {
    Price price = createPrice();

    BarParameters params = new BarParameters().put(price);

    assertValidPriceParams(params, price);
  }

  private Price createPrice() {
    Price price = new Price();
    price.setOpen(10d);
    price.setHigh(20d);
    price.setLow(30d);
    price.setClose(40d);
    price.setVolume(100000);
    return price;
  }

  private void assertValidPriceParams(BarParameters params, Price price) {
    assertEquals(params.get(DataType.PRICE_OPEN), price.getOpen());
    assertEquals(params.get(DataType.PRICE_HIGH), price.getHigh());
    assertEquals(params.get(DataType.PRICE_LOW), price.getLow());
    assertEquals(params.get(DataType.PRICE_CLOSE), price.getClose());
    assertEquals((long) params.get(DataType.VOLUME), price.getVolume());
  }

  @Test
  public void testGetIchimoku() {
    Ichimoku ichimoku = createIchimoku();

    BarParameters params = new BarParameters().put(ichimoku);

    assertValidIchimokuParams(params, ichimoku);
  }

  private Ichimoku createIchimoku() {
    Ichimoku ichimoku = new Ichimoku();
    ichimoku.setTenkan(10d);
    ichimoku.setKijun(20d);
    ichimoku.setChikou(30d);
    ichimoku.setSenkouA(40d);
    ichimoku.setSenkouB(50d);
    return ichimoku;
  }

  private void assertValidIchimokuParams(BarParameters params, Ichimoku ichimoku) {
    assertEquals(params.get(DataType.TENKAN), ichimoku.getTenkan());
    assertEquals(params.get(DataType.KIJUN), ichimoku.getKijun());
    assertEquals(params.get(DataType.CHIKOU), ichimoku.getChikou());
    assertEquals(params.get(DataType.SENKOU_A), ichimoku.getSenkouA());
    assertEquals(params.get(DataType.SENKOU_B), ichimoku.getSenkouB());
  }
}

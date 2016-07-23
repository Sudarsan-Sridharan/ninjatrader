package com.bn.ninjatrader.common.util;

import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.common.data.Price;

import java.util.Random;

import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 5/27/16.
 */
public class TestUtil {

  private static final Random random = new Random();

  public static void assertPriceEquals(Price price1, Price price2) {
    assertEquals(price1.getOpen(), price2.getOpen());
    assertEquals(price1.getHigh(), price2.getHigh());
    assertEquals(price1.getLow(), price2.getLow());
    assertEquals(price1.getClose(), price2.getClose());
    assertEquals(price1.getVolume(), price2.getVolume());
    assertEquals(price1.getDate(), price2.getDate());
  }

  public static void assertIchimokuEquals(Ichimoku ichimoku1, Ichimoku ichimoku2) {
    assertEquals(ichimoku1.getDate(), ichimoku2.getDate());
    assertEquals(ichimoku1.getChikou(), ichimoku2.getChikou());
    assertEquals(ichimoku1.getTenkan(), ichimoku2.getTenkan());
    assertEquals(ichimoku1.getKijun(), ichimoku2.getKijun());
    assertEquals(ichimoku1.getSenkouA(), ichimoku2.getSenkouA());
    assertEquals(ichimoku1.getSenkouB(), ichimoku2.getSenkouB());
  }
  
  public static Price randomPrice() {
    return randomPrice(1, random.nextInt(100));
  }

  public static Price randomPrice(double lowest, double highest) {
    Price price = new Price();
    price.setOpen(random.nextDouble());
    price.setHigh(random.nextInt((int)highest));
    price.setLow(random.nextDouble() + lowest);
    price.setClose(random.nextDouble());
    price.setVolume(random.nextLong());
    return price;
  }


}

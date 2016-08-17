package com.bn.ninjatrader.common.util;

import com.bn.ninjatrader.common.data.DailyQuote;
import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.common.data.Price;
import com.google.common.collect.Lists;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static org.testng.Assert.assertEquals;

/**
 * Created by Brad on 5/27/16.
 */
public class TestUtil {

  private static final Random random = new Random();

  public static void assertPriceEquals(Price price1, Price price2) {
    assertEquals(price1.getOpen(), price2.getOpen(), "Open price does not match.");
    assertEquals(price1.getHigh(), price2.getHigh(), "High price does not match.");
    assertEquals(price1.getLow(), price2.getLow(), "Low price does not match.");
    assertEquals(price1.getClose(), price2.getClose(), "Close price does not match.");
    assertEquals(price1.getVolume(), price2.getVolume(), "Volume does not match.");
    assertEquals(price1.getDate(), price2.getDate(), "Date does not match.");
  }

  public static void assertIchimokuEquals(Ichimoku ichimoku1, Ichimoku ichimoku2) {
    assertEquals(ichimoku1.getDate(), ichimoku2.getDate(), "Date does not match.");
    assertEquals(ichimoku1.getChikou(), ichimoku2.getChikou(), "Chikou does not match.");
    assertEquals(ichimoku1.getTenkan(), ichimoku2.getTenkan(), "Tenkan does not match.");
    assertEquals(ichimoku1.getKijun(), ichimoku2.getKijun(), "Kijun does not match.");
    assertEquals(ichimoku1.getSenkouA(), ichimoku2.getSenkouA(), "SenkouA does not match.");
    assertEquals(ichimoku1.getSenkouB(), ichimoku2.getSenkouB(), "SenkouB does not match.");
  }
  
  public static Price randomPrice() {
    return randomPriceWithFloorCeil(1, random.nextInt(100));
  }

  public static List<Price> randomPricesForDateRange(LocalDate fromDate, LocalDate toDate) {
    LocalDate currentDate = fromDate;
    if (!DateUtil.isWeekday(currentDate)) {
      currentDate = nextWeekday(currentDate);
    }

    List<Price> prices = Lists.newArrayList();
    while (currentDate.isBefore(toDate)) {
      Price price = randomPrice();
      price.setDate(currentDate);
      prices.add(price);
      currentDate = nextWeekday(currentDate);
    }
    return prices;
  }

  public static LocalDate nextWeekday(LocalDate date) {
    date = date.plusDays(1);
    if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
      date = date.plusDays(2);
    } else if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
      date = date.plusDays(1);
    }
    return date;
  }

  public static Price randomPriceWithFloorCeil(double lowest, double highest) {
    Price price = new Price();
    price.setOpen(random.nextDouble());
    price.setHigh(((random.nextDouble() * 1000) + lowest) % highest);
    price.setLow(random.nextDouble() + lowest);
    price.setClose(random.nextDouble());
    price.setVolume(random.nextLong());
    return price;
  }

  public static List<Price> randomPrices(int count) {
    List<Price> prices = Lists.newArrayList();
    LocalDate date = LocalDate.of(2016, 1, 1);

    for (int i = 0; i < count; i++) {
      Price price = randomPrice();
      price.setDate(date);
      prices.add(price);
      date = date.plusDays(1);
    }
    return prices;
  }

  public static void assertDailyQuoteEquals(DailyQuote actual, DailyQuote expected) {
    assertEquals(actual.getSymbol(), expected.getSymbol(), "Symbols do not match");
    assertPriceEquals(actual.getPrice(), expected.getPrice());
  }

  public static void assertPriceEqualsQuote(Price price, DailyQuote quote) {
    assertPriceEquals(price, quote.getPrice());
  }
}

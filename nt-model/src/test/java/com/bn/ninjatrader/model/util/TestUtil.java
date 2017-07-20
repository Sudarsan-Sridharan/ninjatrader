package com.bn.ninjatrader.model.util;

import com.bn.ninjatrader.common.util.DateUtil;
import com.bn.ninjatrader.model.deprecated.Value;
import com.bn.ninjatrader.model.entity.Price;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;

/**
 * Created by Brad on 5/27/16.
 */
public class TestUtil {
  private static final Logger LOG = LoggerFactory.getLogger(TestUtil.class);
  private static final Random random = new Random();
  private static final ZoneId zoneId = ZoneId.systemDefault();
  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final LocalDate now = LocalDate.of(2016, 2, 1);

  public static ObjectMapper objectMapper() {
    return objectMapper;
  }

  public static Clock fixedClock(LocalDate date) {
    return Clock.fixed(date.atStartOfDay(zoneId).toInstant(), zoneId);
  }
  
  public static Price randomPrice() {
    return randomPriceBuilder().build();
  }

  public static Price.Builder randomPriceBuilder() {
    return randomPriceBuilderWithFloorCeil(1, random.nextInt(100));
  }

  public static List<Price> randomPricesForDateRange(final LocalDate fromDate, final LocalDate toDate) {
    // Set date to a weekday
    LocalDate currentDate = fromDate;
    if (!DateUtil.isWeekday(currentDate)) {
      currentDate = DateUtil.nextWeekday(currentDate);
    }

    // Add random prices within given date range.
    final List<Price> prices = Lists.newArrayList();
    while (currentDate.isBefore(toDate) || currentDate.isEqual(toDate)) {
      prices.add(randomPriceBuilder().date(currentDate).build());
      currentDate = DateUtil.nextWeekday(currentDate);
    }
    return prices;
  }

  public static Price randomPriceWithFloorCeil(final double lowest, final double highest) {
    return randomPriceBuilderWithFloorCeil(lowest, highest).build();
  }

  public static Price.Builder randomPriceBuilderWithFloorCeil(final double lowest, final double highest) {
    return Price.builder()
        .open(random.nextDouble())
        .high(((random.nextDouble() * 1000) + lowest) % highest)
        .low(random.nextDouble() + lowest)
        .close(random.nextDouble())
        .volume(random.nextLong())
        .change(random.nextBoolean() ? random.nextDouble() : -random.nextDouble());
  }

  public static List<Price> randomPrices(final int count) {
    final List<Price> prices = Lists.newArrayList();
    LocalDate date = LocalDate.of(2016, 1, 1);

    // Add random prices with incrementing date
    for (int i = 0; i < count; i++) {
      prices.add(randomPriceBuilder().date(date).build());
      date = date.plusDays(1);
    }
    return prices;
  }

  public static List<Value> randomValues(final int count) {
    final List<Value> values = Lists.newArrayList();
    for (int i = 0; i< count; i++) {
      values.add(randomValue());
    }
    return values;
  }

  public static List<Value> randomValuesForDateRange(final LocalDate from, final LocalDate to) {
    LocalDate currentDate = DateUtil.isWeekday(from) ? from : DateUtil.nextWeekday(from);
    final List<Value> values = Lists.newArrayList();
    while (currentDate.isBefore(to) || currentDate.isEqual(to)) {
      values.add(Value.of(currentDate, random.nextInt(10000)));
      currentDate = DateUtil.nextWeekday(currentDate);
    }
    return values;
  }

  public static Value randomValue() {
    return Value.of(now, random.nextInt(10000));
  }
}

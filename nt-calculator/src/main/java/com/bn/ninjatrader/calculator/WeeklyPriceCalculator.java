package com.bn.ninjatrader.calculator;

import com.bn.ninjatrader.model.entity.Price;
import com.google.common.collect.Lists;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Collection;
import java.util.List;

/**
 * Created by Brad on 5/28/16.
 */
@Singleton
public class WeeklyPriceCalculator {
  private static final Logger LOG = LoggerFactory.getLogger(WeeklyPriceCalculator.class);

  public List<Price> calc(final Collection<Price> priceList) {
    return new OneTimeUseWeeklyPriceCalculator().calc(priceList);
  }

  public List<Price> calc(final Price price, final Price ... more) {
    return new OneTimeUseWeeklyPriceCalculator().calc(Lists.asList(price, more));
  }

  private static class OneTimeUseWeeklyPriceCalculator {
    private static final int MONDAY = 1;
    private static final TemporalField weekOfYearField = WeekFields.ISO.weekOfYear();
    private static final TemporalField dayOfWeekField = WeekFields.ISO.dayOfWeek();

    private int lastRecordedWeekOfYear = 0;
    private int lastRecordedYear = 0;
    private final List<Price> results = Lists.newArrayList();

    private OneTimeUseWeeklyPriceCalculator() {}

    public List<Price> calc(final Collection<Price> priceList) {
      Price.Builder weeklyPriceBuilder = null;

      for (final Price price : priceList) {

        // If price is within the week, add it to the week group
        if (isOnSameWeek(price)) {
          addPriceToWeek(weeklyPriceBuilder, price);
        } else {
          // Otherwise, build the weekly price and start a new week
          if (weeklyPriceBuilder != null) {
            results.add(weeklyPriceBuilder.build());
          }
          weeklyPriceBuilder = createNewWeekWithStartingPrice(price);
          recordNewWeek(weeklyPriceBuilder);
        }
      }

      if (weeklyPriceBuilder != null) {
        results.add(weeklyPriceBuilder.build());
      }

      return results;
    }

    private boolean isOnSameWeek(final Price price) {
      final LocalDate date = price.getDate();
      return lastRecordedWeekOfYear == date.get(weekOfYearField)
          && lastRecordedYear == date.getYear();
    }

    private void addPriceToWeek(final Price.Builder weeklyPriceBuilder, final Price price) {
      weeklyPriceBuilder.high(Math.max(price.getHigh(), weeklyPriceBuilder.getHigh()));
      weeklyPriceBuilder.low(Math.min(price.getLow(), weeklyPriceBuilder.getLow()));
      weeklyPriceBuilder.addVolume(price.getVolume());
      weeklyPriceBuilder.close(price.getClose());
    }

    private Price.Builder createNewWeekWithStartingPrice(final Price price) {
      return Price.builder().copyOf(price).date(price.getDate().with(dayOfWeekField, MONDAY));
    }

    private void recordNewWeek(final Price.Builder weeklyPriceBuilder) {
      final LocalDate date = weeklyPriceBuilder.getDate();
      lastRecordedWeekOfYear = date.get(weekOfYearField);
      lastRecordedYear = date.getYear();
    }
  }
}

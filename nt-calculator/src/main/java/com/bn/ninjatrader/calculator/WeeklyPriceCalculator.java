package com.bn.ninjatrader.calculator;

import com.bn.ninjatrader.common.data.Price;
import com.google.common.collect.Lists;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.List;

/**
 * Created by Brad on 5/28/16.
 */
@Singleton
public class WeeklyPriceCalculator {
  private static final Logger log = LoggerFactory.getLogger(WeeklyPriceCalculator.class);

  public List<Price> calc(List<Price> priceList) {
    return new OneTimeUseWeeklyPriceCalculator().calc(priceList);
  }

  private static class OneTimeUseWeeklyPriceCalculator {
    private static final int MONDAY = 1;
    private static final TemporalField weekOfYearField = WeekFields.ISO.weekOfYear();
    private static final TemporalField dayOfWeekField = WeekFields.ISO.dayOfWeek();

    private int lastRecordedWeekOfYear = 0;
    private int lastRecordedYear = 0;
    private List<Price> results = Lists.newArrayList();

    public List<Price> calc(List<Price> priceList) {
      Price weeklyPrice = null;

      for (Price price : priceList) {
        if (isOnSameWeek(price)) {
          addPriceToWeek(weeklyPrice, price);
        } else {
          weeklyPrice = createNewWeekWithStartingPrice(price);
          recordNewWeek(weeklyPrice);
          results.add(weeklyPrice);
        }
      }

      return results;
    }

    private boolean isOnSameWeek(Price price) {
      LocalDate date = price.getDate();
      return lastRecordedWeekOfYear == date.get(weekOfYearField)
          && lastRecordedYear == date.getYear();
    }

    private void addPriceToWeek(Price weeklyPrice, Price price) {
      weeklyPrice.setHigh(Math.max(price.getHigh(), weeklyPrice.getHigh()));
      weeklyPrice.setLow(Math.min(price.getLow(), weeklyPrice.getLow()));
      weeklyPrice.addVolume(price.getVolume());
      weeklyPrice.setClose(price.getClose());
    }

    private Price createNewWeekWithStartingPrice(Price price) {
      Price weeklyPrice = new Price(
          price.getDate().with(dayOfWeekField, MONDAY),
          price.getOpen(),
          price.getHigh(),
          price.getLow(),
          price.getClose(),
          price.getVolume()
      );
      return weeklyPrice;
    }

    private void recordNewWeek(Price price) {
      LocalDate date = price.getDate();
      lastRecordedWeekOfYear = date.get(weekOfYearField);
      lastRecordedYear = date.getYear();
    }
  }
}

package com.bn.ninjatrader.dataimport.daily;

import com.bn.ninjatrader.common.data.DailyQuote;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.request.SaveRequest;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 6/25/16.
 */
public abstract class AbstractDailyPriceImporter {
  private static final Logger LOG = LoggerFactory.getLogger(AbstractDailyPriceImporter.class);

  @Inject
  protected PriceDao priceDao;

  public void importData(final LocalDate date) {
    checkNotNull(date, "date must not be null.");
    importDataForDate(date);
  }

  public void importData(final Collection<LocalDate> dates) {
    checkNotNull(dates, "dates must not be null.");
    checkArgument(!dates.isEmpty(), "dates must not be empty.");

    // Import data for each date.
    dates.forEach(date -> importDataForDate(date));
  }

  private void importDataForDate(final LocalDate date) {
    // Check if weekend
    if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY ) {
      LOG.info("{} is a weekend!", date);
      return;
    }

    for (final DailyQuote quote : provideDailyQuotes(date)) {
      if (quote.getOpen() == 0 || quote.getHigh() == 0 || quote.getLow() == 0 || quote.getClose() == 0) {
        LOG.warn("Ignoring zero value quote: {}", quote);
        continue;
      }

      priceDao.save(SaveRequest.save(quote.getSymbol())
          .timeFrame(TimeFrame.ONE_DAY)
          .values(Lists.newArrayList(quote.getPrice())));
    }
  }

  protected abstract List<DailyQuote> provideDailyQuotes(final LocalDate date);
}

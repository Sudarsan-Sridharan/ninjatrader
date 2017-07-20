package com.bn.ninjatrader.dataimport.daily;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.entity.DailyQuote;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 6/25/16.
 */
public abstract class AbstractDailyPriceImporter {
  private static final Logger LOG = LoggerFactory.getLogger(AbstractDailyPriceImporter.class);

  private final PriceDao priceDao;

  public AbstractDailyPriceImporter(final PriceDao priceDao) {
    this.priceDao = priceDao;
  }

  public List<DailyQuote> importData(final LocalDate date) {
    checkNotNull(date, "date must not be null.");
    final List<DailyQuote> quotes = importDataForDate(date);
    return quotes;
  }

  public List<DailyQuote> importData(final Collection<LocalDate> dates) {
    checkNotNull(dates, "dates must not be null.");
    checkArgument(!dates.isEmpty(), "dates must not be empty.");

    final List<DailyQuote> importedQuotes = Lists.newArrayList();

    // Import data for each date.
    dates.forEach((date) -> {
      importedQuotes.addAll(importData(date));
    });

    return importedQuotes;
  }

  /**
   * Imports data for a given date.
   * @param date Date of data to import
   * @return Imported DailyQuotes
   */
  private List<DailyQuote> importDataForDate(final LocalDate date) {
    // Check if weekend
    if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY ) {
      LOG.info("{} is a weekend!", date);
      return Collections.emptyList();
    }

    final List<DailyQuote> quotes = provideDailyQuotes(date);

    for (final DailyQuote quote : quotes) {
      if (quote.getOpen() == 0 || quote.getHigh() == 0 || quote.getLow() == 0 || quote.getClose() == 0) {
        LOG.warn("Ignoring zero value quote: {}", quote);
        continue;
      }

      priceDao.savePrices(quote.getPrice())
          .withSymbol(quote.getSymbol())
          .withTimeFrame(TimeFrame.ONE_DAY)
          .now();
    }

    return quotes;
  }

  protected abstract List<DailyQuote> provideDailyQuotes(final LocalDate date);
}

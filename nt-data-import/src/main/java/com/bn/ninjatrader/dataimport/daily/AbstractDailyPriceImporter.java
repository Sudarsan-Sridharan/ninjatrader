package com.bn.ninjatrader.dataimport.daily;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.entity.DailyQuote;
import com.bn.ninjatrader.model.entity.PriceBuilderFactory;
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

  private final PriceDao priceDao;
  private final PriceBuilderFactory priceBuilderFactory;

  public AbstractDailyPriceImporter(final PriceDao priceDao,
                                    final PriceBuilderFactory priceBuilderFactory) {
    this.priceDao = priceDao;
    this.priceBuilderFactory = priceBuilderFactory;
  }

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

      priceDao.savePrices(quote.getPrice(priceBuilderFactory))
          .withSymbol(quote.getSymbol())
          .withTimeFrame(TimeFrame.ONE_DAY)
          .now();
    }
  }

  protected abstract List<DailyQuote> provideDailyQuotes(final LocalDate date);
}

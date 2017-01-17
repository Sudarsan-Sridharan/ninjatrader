package com.bn.ninjatrader.dataimport.daily;

import com.bn.ninjatrader.common.data.DailyQuote;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.request.SaveRequest;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Brad on 6/25/16.
 */
public abstract class AbstractDailyPriceImporter {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractDailyPriceImporter.class);

  @Inject
  protected PriceDao priceDao;

  public void importData() throws IOException {

    // Check if weekend
    LocalDate now = LocalDate.now();
    if (now.getDayOfWeek() == DayOfWeek.SATURDAY || now.getDayOfWeek() == DayOfWeek.SUNDAY ) {
      LOG.error("It's a weekend!");
      return;
    }

    for (DailyQuote quote : provideDailyQuotes()) {
        priceDao.save(SaveRequest.save(quote.getSymbol())
            .timeFrame(TimeFrame.ONE_DAY)
            .values(Lists.newArrayList(quote.getPrice())));
    }
  }

  protected abstract List<DailyQuote> provideDailyQuotes();
}

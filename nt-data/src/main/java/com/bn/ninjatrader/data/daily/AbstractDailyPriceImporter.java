package com.bn.ninjatrader.data.daily;

import com.bn.ninjatrader.common.data.DailyQuote;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.dao.StockDao;
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

  private static final Logger log = LoggerFactory.getLogger(AbstractDailyPriceImporter.class);

  @Inject
  protected PriceDao priceDao;

  @Inject
  protected StockDao stockDao;

  public void importData() throws IOException {

    // Check if weekend
    LocalDate now = LocalDate.now();
    if (now.getDayOfWeek() == DayOfWeek.SATURDAY || now.getDayOfWeek() == DayOfWeek.SUNDAY ) {
      log.error("It's a weekend!");
      return;
    }

    List<String> symbols = stockDao.getAllSymbols();

    for (DailyQuote quote : provideDailyQuotes()) {
      if (symbols.contains(quote.getSymbol())) {
        log.debug("Saving: {}", quote);
        priceDao.save(quote.getSymbol(), Lists.newArrayList(quote.getPrice()));
      } else {
        log.debug("Skipping: {}", quote);
      }
    }
  }

  protected abstract List<DailyQuote> provideDailyQuotes();
}

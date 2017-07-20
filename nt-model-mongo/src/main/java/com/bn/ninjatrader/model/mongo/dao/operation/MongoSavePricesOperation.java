package com.bn.ninjatrader.model.mongo.dao.operation;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.model.dao.PriceDao;
import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.mongo.util.Queries;
import com.google.common.collect.Lists;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Builder for finding prices operation
 */
public final class MongoSavePricesOperation implements PriceDao.SavePricesOperation {
  private static final Logger LOG = LoggerFactory.getLogger(MongoSavePricesOperation.class);

  private final MongoCollection mongoCollection;
  private String symbol;
  private TimeFrame timeFrame = TimeFrame.ONE_DAY;
  private Collection<Price> prices = Collections.emptyList();

  public MongoSavePricesOperation(final MongoCollection mongoCollection, Collection<Price> prices) {
    this.mongoCollection = mongoCollection;
    this.prices = prices;
  }

  @Override
  public MongoSavePricesOperation withSymbol(final String symbol) {
    this.symbol = symbol;
    return this;
  }

  @Override
  public MongoSavePricesOperation withTimeFrame(final TimeFrame timeFrame) {
    this.timeFrame = timeFrame;
    return this;
  }

  @Override
  public void now() {
    prices = prices.stream().sorted().collect(Collectors.toList());

    final List<LocalDate> removeDates = Lists.newArrayList();
    final List<Price> perYearPriceList = Lists.newArrayList();

    int currYear = 0;
    for (final Price price : prices) {

      if (currYear != price.getDate().getYear()) {

        // Remove all old values w/ date in new values
        removeByDates(removeDates);

        // Save list of values to year
        saveByYear(currYear, perYearPriceList);

        removeDates.clear();
        perYearPriceList.clear();

        currYear = price.getDate().getYear();
      }

      perYearPriceList.add(price);
      removeDates.add(price.getDate());
    }

    // For the last year
    removeByDates(removeDates);
    saveByYear(currYear, perYearPriceList);
  }

  private void removeByDates(final List<LocalDate> removeDates) {
    if (!removeDates.isEmpty()) {
      final List<String> dates = removeDates.stream()
          .map(date -> date.format(DateTimeFormatter.BASIC_ISO_DATE))
          .collect(Collectors.toList());

      mongoCollection
          .update(Queries.FIND_BY_SYMBOL_TIMEFRAME, symbol, timeFrame)
          .multi()
          .with("{$pull: {data :{d: {$in: #}}}}", dates);
    }
  }

  private void saveByYear(final int year,
                          final List<Price> prices) {
    if (!prices.isEmpty()) {
      // Insert new values
      mongoCollection
          .update(Queries.FIND_BY_SYMBOL_TIMEFRAME_YEAR, symbol, timeFrame, year)
          .upsert()
          .with("{$push: { data: { $each: #, $sort: { d: 1}}}}", prices);
    }
  }
}

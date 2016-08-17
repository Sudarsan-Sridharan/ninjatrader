package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.DateFormats;
import com.bn.ninjatrader.common.util.DateObjUtil;
import com.bn.ninjatrader.common.util.FixedList;
import com.bn.ninjatrader.model.annotation.DailyPriceCollection;
import com.bn.ninjatrader.model.data.AbstractStockData;
import com.bn.ninjatrader.model.data.PriceData;
import com.bn.ninjatrader.model.util.Queries;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Brad on 4/30/16.
 */
@Singleton
public class PriceDao extends AbstractDao<PriceData> {
  private static final Logger log = LoggerFactory.getLogger(PriceDao.class);

  @Inject
  public PriceDao(@DailyPriceCollection MongoCollection mongoCollection) {
    super(mongoCollection);
    mongoCollection.ensureIndex(
            String.format("{%s : 1 ,%s : 1}", AbstractStockData.SYMBOL, AbstractStockData.YEAR), "{unique: true}");
  }

  public List<Price> findByYear(String symbol, int year) {
    PriceData priceData = getMongoCollection().findOne(Queries.FIND_BY_YEAR, symbol, year).as(PriceData.class);

    if (priceData == null || priceData.isEmpty()) {
      return Collections.emptyList();
    }

    return priceData.getData();
  }

  public List<Price> findByDateRange(String symbol, LocalDate fromDate, LocalDate toDate) {
    List<PriceData> priceDataList = Lists.newArrayList(getMongoCollection()
        .find(Queries.FIND_BY_YEAR_RANGE, symbol, fromDate.getYear(), toDate.getYear())
        .as(PriceData.class).iterator());
    List<Price> prices = Lists.newArrayList();

    for (PriceData priceData : priceDataList) {
      prices.addAll(priceData.getData());
    }

    DateObjUtil.trimToDateRange(prices, fromDate, toDate);

    return prices;
  }

  public List<Price> findNBarsBeforeDate(String symbol, int numOfBars, LocalDate beforeDate) {
    List<Price> prices = FixedList.withMaxSize(numOfBars);
    int year = beforeDate.getYear();

    do {
      List<Price> pricesForYear = findByYear(symbol, year);
      DateObjUtil.trimToDateRange(pricesForYear, LocalDate.MIN, beforeDate);
      prices.addAll(0, pricesForYear);
      year--;
    } while (prices.size() < numOfBars && year > 1999);

    return prices;
  }

  public List<PriceData> find() {
    return Lists.newArrayList(getMongoCollection().find().as(PriceData.class).iterator());
  }

  @Override
  public void save(PriceData priceData) {
    Collections.sort(priceData.getData());
    super.save(priceData);
  }

  public void save(String symbol, List<Price> prices) {
    Collections.sort(prices);
    List<LocalDate> removeDates = Lists.newArrayList();
    List<Price> perYearPriceList = Lists.newArrayList();

    int currYear = 0;
    for (Price price : prices) {

      if (currYear != price.getDate().getYear()) {

        // Remove all old values w/ date in new values
        removeByDates(symbol, removeDates);

        // Save list of values to year
        saveToYear(symbol, currYear, perYearPriceList);

        removeDates.clear();
        perYearPriceList.clear();

        currYear = price.getDate().getYear();
      }

      perYearPriceList.add(price);
      removeDates.add(price.getDate());
    }

    // For the last year
    removeByDates(symbol, removeDates);
    saveToYear(symbol, currYear, perYearPriceList);
  }

  public void removeByDates(String symbol, List<LocalDate> removeDates) {
    List<String> dates = Lists.newArrayList();

    for (LocalDate date : removeDates) {
      dates.add(date.format(DateFormats.DB_DATE_FORMAT));
    }

    if (!removeDates.isEmpty()) {
      getMongoCollection().update(Queries.FIND_BY_SYMBOL, symbol).multi()
          .with("{$pull: {data :{d: {$in: #}}}}", dates);
    }
  }

  public void removeByDates(List<LocalDate> removeDates) {
    List<String> dates = Lists.newArrayList();

    for (LocalDate date : removeDates) {
      dates.add(date.format(DateFormats.DB_DATE_FORMAT));
    }

    if (!removeDates.isEmpty()) {
      getMongoCollection().update("{}").multi()
          .with("{$pull: {data :{d: {$in: #}}}}", dates);
    }
  }

  private void saveToYear(String symbol, int year, List<Price> prices) {
    if (!prices.isEmpty()) {
      // Insert new values
      getMongoCollection().update(Queries.FIND_BY_YEAR, symbol, year)
          .upsert()
          .with("{$push: { data: { $each: #, $sort: { d: 1}}}}", prices);
    }
  }

  public List<String> findAllSymbols() {
    int year = LocalDate.now().getYear();
    final List<String> symbols = Lists.newArrayList();

    try (MongoCursor<PriceData> cursor = getMongoCollection()
            .find(Queries.FIND_ALL_FOR_YEAR, year)
            .as(PriceData.class)) {
      cursor.forEach(new Consumer<PriceData>() {
        @Override
        public void accept(PriceData priceData) {
          symbols.add(priceData.getSymbol());
        }
      });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return symbols;
  }
}

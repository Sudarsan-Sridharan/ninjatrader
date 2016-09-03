package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.DateObjUtil;
import com.bn.ninjatrader.common.util.DateUtil;
import com.bn.ninjatrader.common.util.FixedList;
import com.bn.ninjatrader.model.annotation.DailyPriceCollection;
import com.bn.ninjatrader.model.request.FindRequest;
import com.bn.ninjatrader.model.document.PriceDocument;
import com.bn.ninjatrader.model.util.Queries;
import com.bn.ninjatrader.model.util.QueryParamName;
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
public class PriceDao extends AbstractDao<PriceDocument> {

  private static final Logger log = LoggerFactory.getLogger(PriceDao.class);
  private static final LocalDate MINIMUM_FROM_DATE = LocalDate.of(1999, 1, 1);

  @Inject
  public PriceDao(@DailyPriceCollection MongoCollection mongoCollection) {
    super(mongoCollection);
    mongoCollection.ensureIndex(
            String.format("{%s : 1 ,%s : 1}", QueryParamName.SYMBOL, QueryParamName.YEAR), "{unique: true}");
  }

  public List<Price> find(FindRequest findRequest) {
    String symbol = findRequest.getSymbol();
    LocalDate fromDate = findRequest.getFromDate();
    LocalDate toDate = findRequest.getToDate();

    List<PriceDocument> priceDataList = Lists.newArrayList(getMongoCollection()
        .find(Queries.FIND_BY_YEAR_RANGE, symbol, fromDate.getYear(), toDate.getYear())
        .as(PriceDocument.class).iterator());
    List<Price> prices = Lists.newArrayList();

    for (PriceDocument priceData : priceDataList) {
      prices.addAll(priceData.getData());
    }

    DateObjUtil.trimToDateRange(prices, fromDate, toDate);

    return prices;
  }

  public List<Price> findNBarsBeforeDate(String symbol, int numOfBars, LocalDate beforeDate) {
    List<Price> prices = FixedList.withMaxSize(numOfBars);
    LocalDate fromDate = beforeDate.withDayOfYear(1);

    do {
      List<Price> pricesForYear = find(FindRequest.forSymbol(symbol).from(fromDate).to(beforeDate));
      prices.clear();
      prices.addAll(pricesForYear);
      fromDate = fromDate.minusYears(1);
    } while (prices.size() < numOfBars && fromDate.isAfter(MINIMUM_FROM_DATE));

    return prices;
  }

  public List<PriceDocument> find() {
    return Lists.newArrayList(getMongoCollection().find().as(PriceDocument.class).iterator());
  }

  @Override
  public void save(PriceDocument priceData) {
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
    if (!removeDates.isEmpty()) {
      List<String> dates = DateUtil.toListOfString(removeDates);

      getMongoCollection().update(Queries.FIND_BY_SYMBOL, symbol).multi()
          .with("{$pull: {data :{d: {$in: #}}}}", dates);
    }
  }

  public void removeByDates(List<LocalDate> removeDates) {
    if (!removeDates.isEmpty()) {
      List<String> dates = DateUtil.toListOfString(removeDates);

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

    try (MongoCursor<PriceDocument> cursor = getMongoCollection()
            .find(Queries.FIND_ALL_FOR_YEAR, year)
            .as(PriceDocument.class)) {
      cursor.forEach(new Consumer<PriceDocument>() {
        @Override
        public void accept(PriceDocument priceData) {
          symbols.add(priceData.getSymbol());
        }
      });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return symbols;
  }
}

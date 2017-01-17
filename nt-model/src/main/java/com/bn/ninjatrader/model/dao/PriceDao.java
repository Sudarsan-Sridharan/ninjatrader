package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.bn.ninjatrader.common.util.DateObjUtil;
import com.bn.ninjatrader.common.util.DateUtil;
import com.bn.ninjatrader.model.annotation.PriceCollection;
import com.bn.ninjatrader.model.document.PriceDocument;
import com.bn.ninjatrader.model.request.FindRequest;
import com.bn.ninjatrader.model.request.SaveRequest;
import com.bn.ninjatrader.model.util.Queries;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static com.bn.ninjatrader.model.util.QueryParam.*;

/**
 * Created by Brad on 4/30/16.
 */
@Singleton
public class PriceDao extends AbstractDateObjDao<PriceDocument, Price> {

  private static final Logger LOG = LoggerFactory.getLogger(PriceDao.class);

  @Inject
  public PriceDao(@PriceCollection final MongoCollection priceCollection) {
    super(priceCollection);
    priceCollection.ensureIndex(Queries.createIndex(SYMBOL, TIMEFRAME, YEAR),
            "{unique: true}");
  }

  public List<Price> find(final FindRequest findRequest) {
    final LocalDate fromDate = findRequest.getFromDate();
    final LocalDate toDate = findRequest.getToDate();

    final List<PriceDocument> priceDataList = Lists.newArrayList(getMongoCollection()
        .find(Queries.FIND_BY_SYMBOL_TIMEFRAME_YEAR_RANGE, findRequest.getSymbol(),
            findRequest.getTimeFrame(),
            fromDate.getYear(),
            toDate.getYear())
        .as(PriceDocument.class).iterator());
    final List<Price> prices = Lists.newArrayList();

    for (final PriceDocument priceData : priceDataList) {
      prices.addAll(priceData.getData());
    }

    DateObjUtil.trimToDateRange(prices, fromDate, toDate);

    return prices;
  }

  public List<PriceDocument> find() {
    return Lists.newArrayList(getMongoCollection().find().as(PriceDocument.class).iterator());
  }

  @Override
  public void save(final PriceDocument priceDocument) {
    Collections.sort(priceDocument.getData());
    super.save(priceDocument);
  }

  public void save(final SaveRequest<Price> req) {
    Collections.sort(req.getValues());
    final List<LocalDate> removeDates = Lists.newArrayList();
    final List<Price> perYearPriceList = Lists.newArrayList();

    int currYear = 0;
    for (final Price price : req.getValues()) {

      if (currYear != price.getDate().getYear()) {

        // Remove all old values w/ date in new values
        removeByDates(req, removeDates);

        // Save list of values to year
        saveByYear(req, currYear, perYearPriceList);

        removeDates.clear();
        perYearPriceList.clear();

        currYear = price.getDate().getYear();
      }

      perYearPriceList.add(price);
      removeDates.add(price.getDate());
    }

    // For the last year
    removeByDates(req, removeDates);
    saveByYear(req, currYear, perYearPriceList);
  }

  public void removeByDates(final SaveRequest<Price> req, final List<LocalDate> removeDates) {
    if (!removeDates.isEmpty()) {
      final List<String> dates = DateUtil.toListOfString(removeDates);

      getMongoCollection()
          .update(Queries.FIND_BY_SYMBOL_TIMEFRAME, req.getSymbol(), req.getTimeFrame())
          .multi()
          .with("{$pull: {data :{d: {$in: #}}}}", dates);
    }
  }

  public void removeByDates(final List<LocalDate> removeDates) {
    if (!removeDates.isEmpty()) {
      final List<String> dates = DateUtil.toListOfString(removeDates);

      getMongoCollection().update("{}").multi()
          .with("{$pull: {data :{d: {$in: #}}}}", dates);
    }
  }

  private void saveByYear(final SaveRequest<Price> req,
                          final int year,
                          final List<Price> prices) {
    if (!prices.isEmpty()) {
      // Insert new values
      getMongoCollection()
          .update(Queries.FIND_BY_SYMBOL_TIMEFRAME_YEAR, req.getSymbol(), req.getTimeFrame(), year)
          .upsert()
          .with("{$push: { data: { $each: #, $sort: { d: 1}}}}", prices);
    }
  }

  public Collection<String> findAllSymbols() {
    final int thisYear = LocalDate.now().getYear();
    final Set<String> symbols = Sets.newHashSet();

    try (final MongoCursor<PriceDocument> cursor = getMongoCollection()
            .find(Queries.FIND_BY_TIMEFRAME_YEAR, TimeFrame.ONE_DAY, thisYear)
            .as(PriceDocument.class)) {
      cursor.forEach(new Consumer<PriceDocument>() {
        @Override
        public void accept(final PriceDocument priceData) {
          symbols.add(priceData.getSymbol());
        }
      });
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
    return symbols;
  }
}

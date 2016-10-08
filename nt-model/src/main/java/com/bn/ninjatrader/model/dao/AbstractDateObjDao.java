package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.util.FixedList;
import com.bn.ninjatrader.model.request.FindRequest;
import com.google.inject.Singleton;
import org.jongo.MongoCollection;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Brad on 4/30/16.
 */
@Singleton
public abstract class AbstractDateObjDao<T, S> {

  public static final LocalDate MINIMUM_FROM_DATE = LocalDate.of(1999, 1, 1);

  private final MongoCollection mongoCollection;

  public AbstractDateObjDao(MongoCollection mongoCollection) {
    this.mongoCollection = mongoCollection;
  }

  public void save(T t) {
    mongoCollection.save(t);
  }

  public MongoCollection getMongoCollection() {
    return mongoCollection;
  }

  public List<S> findNBarsBeforeDate(String symbol, int numOfBars, LocalDate beforeDate) {
    List<S> prices = FixedList.withMaxSize(numOfBars);
    LocalDate fromDate = beforeDate.withDayOfYear(1);
    LocalDate toDate = beforeDate.minusDays(1);
    do {
      List<S> pricesForYear = find(FindRequest.findSymbol(symbol).from(fromDate).to(toDate));
      prices.clear();
      prices.addAll(pricesForYear);
      fromDate = fromDate.minusYears(1);

      // If not enough data, reset the list and search again with a wider date range.
    } while (prices.size() < numOfBars && fromDate.isAfter(MINIMUM_FROM_DATE));

    return prices;
  }

  protected abstract List<S> find(FindRequest to);
}

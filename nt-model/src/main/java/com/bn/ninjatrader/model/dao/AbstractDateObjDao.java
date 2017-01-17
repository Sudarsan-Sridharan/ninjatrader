package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.util.FixedList;
import com.bn.ninjatrader.model.request.FindBeforeDateRequest;
import com.bn.ninjatrader.model.request.FindRequest;
import com.google.inject.Singleton;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Brad on 4/30/16.
 */
@Singleton
public abstract class AbstractDateObjDao<T, S> extends AbstractDao<T> {
  private static final Logger LOG = LoggerFactory.getLogger(AbstractDateObjDao.class);

  public AbstractDateObjDao(final MongoCollection dailyCollection) {
    super(dailyCollection);
  }

  public List<S> findBeforeDate(final FindBeforeDateRequest request) {
    final List<S> bars = FixedList.withMaxSize(request.getNumOfValues());
    LocalDate fromDate = request.getBeforeDate().withDayOfYear(1);
    LocalDate toDate = request.getBeforeDate().minusDays(1);

    do {
      final List<S> resultsPerYear = find(FindRequest.findSymbol(request.getSymbol())
          .timeframe(request.getTimeFrame())
          .from(fromDate)
          .to(toDate)
          .period(request.getPeriod()));
      bars.clear();
      bars.addAll(resultsPerYear);
      fromDate = fromDate.minusYears(1);

      // If not enough data, reset the list and search again with a wider date range.
    } while (bars.size() < request.getNumOfValues() && fromDate.isAfter(MINIMUM_FROM_DATE));

    return bars;
  }

  public abstract List<S> find(FindRequest findRequest);
}

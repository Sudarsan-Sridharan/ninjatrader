package com.bn.ninjatrader.model.dao.period;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.util.DateFormats;
import com.bn.ninjatrader.common.util.DateObjUtil;
import com.bn.ninjatrader.model.dao.AbstractDao;
import com.bn.ninjatrader.model.dao.SaveRequest;
import com.bn.ninjatrader.model.data.PeriodData;
import com.bn.ninjatrader.model.util.Queries;
import com.bn.ninjatrader.model.util.QueryParamName;
import com.google.common.collect.Lists;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by Brad on 4/30/16.
 */
public abstract class AbstractPeriodDao extends AbstractDao<Value> {

  private static final Logger log = LoggerFactory.getLogger(AbstractPeriodDao.class);

  public AbstractPeriodDao(MongoCollection mongoCollection) {
    super(mongoCollection);

    getMongoCollection().ensureIndex(
            String.format(" { %s : 1, %s : 1, %s : 1} ",
                QueryParamName.SYMBOL, QueryParamName.YEAR, QueryParamName.PERIOD), "{ unique: true }");
  }

  public Optional<PeriodData> findByPeriod(String symbol, int year, int period) {
    PeriodData data = getMongoCollection().findOne(Queries.FIND_BY_PERIOD, symbol, year, period).as(PeriodData.class);
    return Optional.ofNullable(data);
  }

  public List<Value> findByDateRange(String symbol, int period, LocalDate fromDate, LocalDate toDate) {
    List<PeriodData> periodDataList = Lists.newArrayList(getMongoCollection()
            .find(Queries.FIND_BY_PERIOD_YEAR_RANGE, symbol, fromDate.getYear(), toDate.getYear(), period)
            .as(PeriodData.class).iterator());
    List<Value> values = Lists.newArrayList();

    for (PeriodData periodData : periodDataList) {
      values.addAll(periodData.getData());
    }

    DateObjUtil.trimToDateRange(values, fromDate, toDate);

    return values;
  }

  public void save(PeriodData t) {
    Collections.sort(t.getData());
    getMongoCollection().save(t);
  }

  public void save(String symbol, int period, List<Value> values) {
    List<String> removeDates = Lists.newArrayList();
    List<Value> perYearValueList = Lists.newArrayList();

    Collections.sort(values);

    int currYear = -1;

    for (Value v : values) {

      // Collect all values of same year til year has changed
      if (currYear != v.getDate().getYear()) {

        // Remove all old values w/ date in new values
        removeByDates(symbol, currYear, period, removeDates);

        // Save list of values to year
        saveToYear(symbol, currYear, period, perYearValueList);

        // Reset collections for new year
        removeDates.clear();
        perYearValueList.clear();
        currYear = v.getDate().getYear();
      }

      perYearValueList.add(v);
      removeDates.add(v.getDate().format(DateFormats.DB_DATE_FORMAT));
    }

    // For the last year
    removeByDates(symbol, currYear, period, removeDates);
    saveToYear(symbol, currYear, period, perYearValueList);
  }

  private void removeByDates(String symbol, int year, int period, List<String> dates) {
    if (!dates.isEmpty()) {
      // Remove all existing w/ same dates
      getMongoCollection().update(Queries.FIND_BY_PERIOD, symbol, year, period)
              .with("{$pull: {data :{d: {$in: #}}}}", dates);
    }
  }

  private void saveToYear(String symbol, int year, int period, List<Value> values) {
    if (!values.isEmpty()) {
      // Insert new values
      getMongoCollection().update(Queries.FIND_BY_PERIOD, symbol, year, period)
              .upsert()
              .with("{$push: { data: { $each: #, $sort: { d: 1}}}}", values);
    }
  }

  public SaveRequest requestSave() {
    return new SaveRequest(this);
  }
}

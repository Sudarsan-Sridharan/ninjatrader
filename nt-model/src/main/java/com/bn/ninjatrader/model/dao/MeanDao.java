package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.util.DateObjUtil;
import com.bn.ninjatrader.common.util.NtConst;
import com.bn.ninjatrader.model.annotation.DailyMeanCollection;
import com.bn.ninjatrader.model.data.AbstractStockData;
import com.bn.ninjatrader.model.data.MeanData;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jongo.MongoCollection;
import org.jongo.Oid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by Brad on 4/30/16.
 */
@Singleton
public class MeanDao {

  private static final Logger log = LoggerFactory.getLogger(MeanDao.class);

  private static final String QUERY_FIND_BY_PERIOD = String.format("{%s : #, %s: #, %s: #}",
          MeanData.SYMBOL, MeanData.YEAR, MeanData.PERIOD);

  public static final String QUERY_FIND_BY_YEAR_RANGE = String.format("{%s : #, %s: {$gte: #, $lte: #}, %s: #}",
          AbstractStockData.SYMBOL, AbstractStockData.YEAR, MeanData.PERIOD);

  private final MongoCollection mongoCollection;

  @Inject
  public MeanDao(@DailyMeanCollection MongoCollection mongoCollection) {
    this.mongoCollection = mongoCollection;

    getMongoCollection().ensureIndex(
            String.format(" { %s : 1, %s : 1, %s : 1} ", MeanData.SYMBOL, MeanData.YEAR, MeanData.PERIOD),
            "{ unique: true }");
  }

  public Optional<MeanData> findByPeriod(String symbol, int year, int period) {
    MeanData data = mongoCollection.findOne(QUERY_FIND_BY_PERIOD, symbol, year, period).as(MeanData.class);
    return Optional.ofNullable(data);
  }

  public List<Value> findByDateRange(String symbol, int period, LocalDate fromDate, LocalDate toDate) {
    List<MeanData> meanDataList = Lists.newArrayList(getMongoCollection()
            .find(QUERY_FIND_BY_YEAR_RANGE, symbol, fromDate.getYear(), toDate.getYear(), period)
            .as(MeanData.class).iterator());
    List<Value> values = Lists.newArrayList();

    for (MeanData meanData : meanDataList) {
      values.addAll(meanData.getData());
    }

    DateObjUtil.trimToDateRange(values, fromDate, toDate);

    return values;
  }

  public List<MeanData> find() {
    return Lists.newArrayList(mongoCollection.find().as(MeanData.class).iterator());
  }

  public void save(MeanData t) {
    Collections.sort(t.getData());
    mongoCollection.save(t);
  }

  public void remove(String id) {
    mongoCollection.remove(Oid.withOid(id));
  }

  public void insert(MeanData t) {
    mongoCollection.insert(t);
  }

  public MongoCollection getMongoCollection() {
    return mongoCollection;
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
      removeDates.add(v.getDate().format(NtConst.DB_DATE_FORMAT));
    }

    // For the last year
    removeByDates(symbol, currYear, period, removeDates);
    saveToYear(symbol, currYear, period, perYearValueList);
  }

  private void removeByDates(String symbol, int year, int period, List<String> dates) {
    if (!dates.isEmpty()) {
      // Remove all existing w/ same dates
      mongoCollection.update(QUERY_FIND_BY_PERIOD, symbol, year, period)
              .with("{$pull: {data :{d: {$in: #}}}}", dates);
    }
  }

  private void saveToYear(String symbol, int year, int period, List<Value> values) {
    if (!values.isEmpty()) {
      // Insert new values
      mongoCollection.update(QUERY_FIND_BY_PERIOD, symbol, year, period)
              .upsert()
              .with("{$push: { data: { $each: #, $sort: { d: 1}}}}", values);
    }
  }
}

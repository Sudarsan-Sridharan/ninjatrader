package com.bn.ninjatrader.model.dao.period;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.util.DateObjUtil;
import com.bn.ninjatrader.model.data.PeriodData;
import com.bn.ninjatrader.model.util.Queries;
import com.google.common.collect.Lists;
import org.jongo.MongoCollection;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Brad on 7/28/16.
 */
public class PeriodDataSaver {

  private MongoCollection mongoCollection;

  public PeriodDataSaver(MongoCollection mongoCollection) {
    this.mongoCollection = mongoCollection;
  }

  protected List<Value> findByDateRange(String symbol, int period, LocalDate fromDate, LocalDate toDate) {
    List<PeriodData> periodDataList = Lists.newArrayList(mongoCollection
        .find(Queries.FIND_BY_PERIOD_YEAR_RANGE, symbol, fromDate.getYear(), toDate.getYear(), period)
        .as(PeriodData.class).iterator());

    List<Value> values = mergeValues(periodDataList);

    DateObjUtil.trimToDateRange(values, fromDate, toDate);
    return values;
  }

  private List<Value> mergeValues(List<PeriodData> periodDataList) {
    List<Value> values = Lists.newArrayList();
    for (PeriodData periodData : periodDataList) {
      values.addAll(periodData.getData());
    }
    return values;
  }
}

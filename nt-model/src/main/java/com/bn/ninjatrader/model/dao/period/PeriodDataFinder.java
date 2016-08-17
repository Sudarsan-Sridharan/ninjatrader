package com.bn.ninjatrader.model.dao.period;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.util.DateObjUtil;
import com.bn.ninjatrader.model.data.PeriodData;
import com.bn.ninjatrader.model.util.Queries;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jongo.MongoCollection;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Brad on 7/28/16.
 */
public class PeriodDataFinder {

  private MongoCollection mongoCollection;

  protected PeriodDataFinder(MongoCollection mongoCollection) {
    this.mongoCollection = mongoCollection;
  }

  protected List<Value> find(FindRequest findRequest) {
    assertPreconditions(findRequest);
    setDefaultValues(findRequest);

    LocalDate fromDate = findRequest.getFromDate();
    LocalDate toDate = findRequest.getToDate();

    List<PeriodData> periodDataList = Lists.newArrayList(mongoCollection
        .find(Queries.FIND_BY_PERIOD_YEAR_RANGE,
            findRequest.getSymbol(), fromDate.getYear(), toDate.getYear(), findRequest.getPeriod())
        .as(PeriodData.class).iterator());

    List<Value> values = mergeValues(periodDataList);

    DateObjUtil.trimToDateRange(values, fromDate, toDate);
    return values;
  }

  private void assertPreconditions(FindRequest findRequest) {
    Preconditions.checkArgument(StringUtils.isNotEmpty(findRequest.getSymbol()));
    Preconditions.checkArgument(findRequest.getPeriod() > 0);
  }

  private void setDefaultValues(FindRequest findRequest) {
    if (findRequest.getFromDate() == null) {
      findRequest.from(LocalDate.now().minusYears(1));
    }
    if (findRequest.getToDate() == null) {
      findRequest.to(LocalDate.now());
    }
  }

  private List<Value> mergeValues(List<PeriodData> periodDataList) {
    List<Value> values = Lists.newArrayList();
    for (PeriodData periodData : periodDataList) {
      values.addAll(periodData.getData());
    }
    return values;
  }
}

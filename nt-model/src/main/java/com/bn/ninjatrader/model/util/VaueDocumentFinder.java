package com.bn.ninjatrader.model.util;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.util.DateObjUtil;
import com.bn.ninjatrader.model.request.FindRequest;
import com.bn.ninjatrader.model.document.ValueDocument;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Brad on 7/28/16.
 */
public class VaueDocumentFinder<T extends Value> {

  private static final Logger log = LoggerFactory.getLogger(VaueDocumentFinder.class);

  private MongoCollection mongoCollection;
  private Class documentClass;

  public VaueDocumentFinder(MongoCollection mongoCollection, Class documentClass) {
    this.mongoCollection = mongoCollection;
    this.documentClass = documentClass;
  }

  public List<T> find(FindRequest findRequest) {
    assertPreconditions(findRequest);
    setDefaultValues(findRequest);

    LocalDate fromDate = findRequest.getFromDate();
    LocalDate toDate = findRequest.getToDate();

    List<ValueDocument> periodDataList = Lists.newArrayList(mongoCollection
        .find(Queries.FIND_BY_PERIOD_YEAR_RANGE,
            findRequest.getSymbol(), fromDate.getYear(), toDate.getYear(), findRequest.getPeriod())
        .as(documentClass).iterator());

    List<T> values = mergeValues(periodDataList);

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

  private List<T> mergeValues(List<ValueDocument> periodDataList) {
    List<T> values = Lists.newArrayList();
    for (ValueDocument periodData : periodDataList) {
      values.addAll(periodData.getData());
    }
    return values;
  }
}

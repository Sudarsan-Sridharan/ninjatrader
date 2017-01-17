package com.bn.ninjatrader.model.util;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.util.DateObjUtil;
import com.bn.ninjatrader.model.document.ValueDocument;
import com.bn.ninjatrader.model.request.FindRequest;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by Brad on 7/28/16.
 */
public class ValueDocumentFinder<T extends Value> {

  private static final Logger LOG = LoggerFactory.getLogger(ValueDocumentFinder.class);

  private final MongoCollection mongoCollection;
  private final Class documentClass;

  public ValueDocumentFinder(MongoCollection mongoCollections, Class documentClass) {
    this.mongoCollection = mongoCollections;
    this.documentClass = documentClass;
  }

  public List<T> find(final FindRequest findRequest) {
    assertPreconditions(findRequest);
    setDefaultValues(findRequest);

    final LocalDate fromDate = findRequest.getFromDate();
    final LocalDate toDate = findRequest.getToDate();

    final List<ValueDocument> valueDocuments = Lists.newArrayList(mongoCollection.find(Queries.FIND_BY_PERIOD_YEAR_RANGE,
        findRequest.getSymbol(),
        findRequest.getTimeFrame(),
        fromDate.getYear(),
        toDate.getYear(),
        findRequest.getPeriod())
        .as(documentClass).iterator());

    final List<T> values = mergeDocumentsToValues(valueDocuments);

    DateObjUtil.trimToDateRange(values, fromDate, toDate);

    return values;
  }

  private void assertPreconditions(final FindRequest findRequest) {
    checkArgument(StringUtils.isNotEmpty(findRequest.getSymbol()), "Symbol must not be empty.");
    checkArgument(findRequest.getPeriod() > 0, "Period must be > 0.");
  }

  private void setDefaultValues(final FindRequest findRequest) {
    if (findRequest.getFromDate() == null) {
      findRequest.from(LocalDate.now().minusYears(100));
    }
    if (findRequest.getToDate() == null) {
      findRequest.to(LocalDate.now());
    }
  }

  private List<T> mergeDocumentsToValues(List<ValueDocument> periodDataList) {
    List<T> values = Lists.newArrayList();
    for (ValueDocument periodData : periodDataList) {
      values.addAll(periodData.getData());
    }
    return values;
  }
}

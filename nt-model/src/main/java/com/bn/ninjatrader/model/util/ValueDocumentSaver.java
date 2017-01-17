package com.bn.ninjatrader.model.util;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.util.DateFormats;
import com.bn.ninjatrader.model.request.SaveRequest;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * Created by Brad on 7/28/16.
 */
public class ValueDocumentSaver {

  private static final Logger LOG = LoggerFactory.getLogger(ValueDocumentSaver.class);

  private final MongoCollection mongoCollection;

  public ValueDocumentSaver(MongoCollection mongoCollections) {
    this.mongoCollection = mongoCollections;
  }

  public void save(SaveRequest saveRequest) {
    assertPreconditions(saveRequest);
    if (!saveRequest.hasValues()) {
      return;
    }

    List<ValuesPerYear> valuesPerYearList = splitToValuesPerYear(saveRequest.getValues());
    saveValuesPerYear(saveRequest, valuesPerYearList);
  }

  private void assertPreconditions(SaveRequest saveRequest) {
    Preconditions.checkArgument(StringUtils.isNotEmpty(saveRequest.getSymbol()));
    Preconditions.checkArgument(saveRequest.getPeriod() > 0);
  }

  private List<ValuesPerYear> splitToValuesPerYear(List<? extends Value> values) {
    Collections.sort(values);
    List<ValuesPerYear> valuesPerYearList = Lists.newArrayList();

    ValuesPerYear valuesPerYear = null;
    int currYear = 0;
    for (Value value : values) {
      // Collect all values of same year til year has changed
      if (currYear != value.getDate().getYear()) {
        currYear = value.getDate().getYear();
        valuesPerYear = new ValuesPerYear();
        valuesPerYear.setYear(currYear);
        valuesPerYearList.add(valuesPerYear);
      }
      valuesPerYear.addValue(value);
    }
    return valuesPerYearList;
  }

  private void saveValuesPerYear(SaveRequest saveRequest, List<ValuesPerYear> valuesPerYearList) {
    for (ValuesPerYear valuesPerYear : valuesPerYearList) {
      removeByDates(saveRequest, valuesPerYear);
      saveByYearAndPeriod(saveRequest, valuesPerYear);
    }
  }

  /**
   * Remove all existing Values w/ same dates
   */
  private void removeByDates(SaveRequest saveRequest, ValuesPerYear valuesPerYear) {
    if (!valuesPerYear.getDatesToRemove().isEmpty()) {
      mongoCollection.update(Queries.FIND_BY_PERIOD,
          saveRequest.getSymbol(),
          saveRequest.getTimeFrame(),
          valuesPerYear.getYear(),
          saveRequest.getPeriod())
          .with("{$pull: { data: {d: {$in: #}}}}", valuesPerYear.getDatesToRemove());
    }
  }

  private void saveByYearAndPeriod(SaveRequest saveRequest, ValuesPerYear valuesPerYear) {
    if (!valuesPerYear.getValues().isEmpty()) {
      // Insert new values
      mongoCollection.update(Queries.FIND_BY_PERIOD,
          saveRequest.getSymbol(),
          saveRequest.getTimeFrame(),
          valuesPerYear.getYear(),
          saveRequest.getPeriod())
          .upsert()
          .with("{$push: { data: { $each: #, $sort: { d: 1}}}}", valuesPerYear.getValues());
    }
  }

  private static class ValuesPerYear {
    private int year;
    private List<Value> values;
    private List<String> datesToRemove;

    public ValuesPerYear() {
      this.values = Lists.newArrayList();
      this.datesToRemove = Lists.newArrayList();
    }

    public int getYear() {
      return year;
    }

    public void setYear(int year) {
      this.year = year;
    }

    public List<Value> getValues() {
      return values;
    }

    public void addValue(Value value) {
      values.add(value);
      datesToRemove.add(value.getDate().format(DateFormats.DB_DATE_FORMAT));
    }

    public List<String> getDatesToRemove() {
      return datesToRemove;
    }
  }
}

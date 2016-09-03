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

  private static final Logger log = LoggerFactory.getLogger(ValueDocumentSaver.class);

  private MongoCollection mongoCollection;

  public ValueDocumentSaver(MongoCollection mongoCollection) {
    this.mongoCollection = mongoCollection;
  }

  public void save(SaveRequest saveRequest) {
    assertPreconditions(saveRequest);
    if (!saveRequest.hasValues()) {
      return;
    }

    List<ValuesPerYear> valuesPerYearList = splitToValuesPerYear(saveRequest.getValues());
    saveValuesPerYear(saveRequest.getSymbol(), saveRequest.getPeriod(), valuesPerYearList);
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

  private void saveValuesPerYear(String symbol, int period, List<ValuesPerYear> valuesPerYearList) {
    for (ValuesPerYear valuesPerYear : valuesPerYearList) {
      int year = valuesPerYear.getYear();
      removeByDates(symbol, year, period, valuesPerYear.getDatesToRemove());
      saveByYearAndPeriod(symbol, year, period, valuesPerYear.getValues());
    }
  }

  /**
   * Remove all existing Values w/ same dates
   */
  private void removeByDates(String symbol, int year, int period, List<String> dates) {
    if (!dates.isEmpty()) {
      mongoCollection.update(Queries.FIND_BY_PERIOD, symbol, year, period)
          .with("{$pull: { data: {d: {$in: #}}}}", dates);
    }
  }

  private void saveByYearAndPeriod(String symbol, int year, int period, List<? extends Value> values) {
    if (!values.isEmpty()) {
      // Insert new values
      mongoCollection.update(Queries.FIND_BY_PERIOD, symbol, year, period)
          .upsert()
          .with("{$push: { data: { $each: #, $sort: { d: 1}}}}", values);
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

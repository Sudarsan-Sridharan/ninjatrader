package com.bn.ninjatrader.model.util;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.util.DateFormats;
import com.bn.ninjatrader.model.request.SaveRequest;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by Brad on 7/28/16.
 */
public class ValueDocumentSaver {
  private static final Logger LOG = LoggerFactory.getLogger(ValueDocumentSaver.class);

  private final MongoCollection mongoCollection;

  public ValueDocumentSaver(final MongoCollection mongoCollections) {
    this.mongoCollection = mongoCollections;
  }

  public void save(final SaveRequest saveRequest) {
    checkArgument(StringUtils.isNotEmpty(saveRequest.getSymbol()));
    checkArgument(saveRequest.getPeriod() > 0);

    if (!saveRequest.hasValues()) {
      return;
    }

    final List<ValuesPerYear> valuesPerYearList = splitToValuesPerYear(saveRequest.getValues());
    saveValuesPerYear(saveRequest, valuesPerYearList);
  }

  private List<ValuesPerYear> splitToValuesPerYear(final List<? extends Value> values) {
    Collections.sort(values);
    final List<ValuesPerYear> valuesPerYearList = Lists.newArrayList();

    ValuesPerYear valuesPerYear = null;
    int currYear = 0;
    for (final Value value : values) {
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

  private void saveValuesPerYear(final SaveRequest saveRequest, final List<ValuesPerYear> valuesPerYearList) {
    for (final ValuesPerYear valuesPerYear : valuesPerYearList) {
      removeByDates(saveRequest, valuesPerYear);
      saveByYearAndPeriod(saveRequest, valuesPerYear);
    }
  }

  /**
   * Remove all existing Values w/ same dates
   */
  private void removeByDates(final SaveRequest saveRequest, final ValuesPerYear valuesPerYear) {
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

  /**
   * ValuesPerYear Class
   */
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

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this)
          .add("year", year)
          .add("datesToRemove", datesToRemove)
          .add("values", values).toString();
    }
  }
}

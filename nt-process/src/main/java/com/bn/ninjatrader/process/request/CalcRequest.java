package com.bn.ninjatrader.process.request;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by Brad on 7/28/16.
 */
public class CalcRequest {

  private Set<String> symbolSet = Sets.newHashSet();
  private LocalDate fromDate;
  private LocalDate toDate;
  private final List<Integer> periods = Lists.newArrayList();
  private final List<TimeFrame> timeFrames = Lists.newArrayList();

  public static CalcRequest forSymbol(final String symbol) {
    return new CalcRequest().symbol(symbol);
  }

  public static CalcRequest forSymbols(final String symbol, final String ... more) {
    return new CalcRequest().symbols(symbol, more);
  }

  public static CalcRequest forSymbols(final Collection<String> symbols) {
    return new CalcRequest().symbols(symbols);
  }

  private CalcRequest() {
    allTimeFrames();
  }

  public CalcRequest symbol(final String symbol) {
    symbolSet.clear();
    symbolSet.add(symbol);
    return this;
  }

  public CalcRequest symbols(final String symbol, final String ... more) {
    symbolSet.clear();
    symbolSet.addAll(Lists.asList(symbol, more));
    return this;
  }

  public CalcRequest symbols(final Collection<String> symbols) {
    symbolSet.clear();
    symbolSet.addAll(symbols);
    return this;
  }

  public CalcRequest from(final LocalDate fromDate) {
    this.fromDate = fromDate;
    return this;
  }

  public CalcRequest to(final LocalDate toDate) {
    this.toDate = toDate;
    return this;
  }

  public CalcRequest periods(final int period) {
    this.periods.add(period);
    return this;
  }

  public CalcRequest periods(final Collection<Integer> periods) {
    if (periods != null) {
      this.periods.addAll(periods);
    }
    return this;
  }

  public CalcRequest periods(final Integer period, final Integer ... more) {
    this.periods.addAll(Lists.asList(period, more));
    return this;
  }

  public CalcRequest timeFrames(final TimeFrame timeFrame) {
    timeFrames.clear();
    timeFrames.add(timeFrame);
    return this;
  }

  public CalcRequest timeFrames(final TimeFrame timeFrame, final TimeFrame ... more) {
    timeFrames.clear();
    timeFrames.addAll(Lists.asList(timeFrame, more));
    return this;
  }

  public CalcRequest allTimeFrames() {
    timeFrames.clear();
    timeFrames.addAll(Arrays.asList(TimeFrame.values()));
    return this;
  }

  public Collection<String> getAllSymbols() {
    return symbolSet;
  }

  public List<TimeFrame> getTimeFrames() {
    return Lists.newArrayList(timeFrames);
  }

  public LocalDate getFromDate() {
    return fromDate;
  }

  public LocalDate getToDate() {
    return toDate;
  }

  public List<Integer> getPeriods() {
    return Lists.newArrayList(periods);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("symbols", symbolSet)
        .add("timeFrame", timeFrames)
        .add("from", fromDate)
        .add("to", toDate)
        .toString();
  }
}

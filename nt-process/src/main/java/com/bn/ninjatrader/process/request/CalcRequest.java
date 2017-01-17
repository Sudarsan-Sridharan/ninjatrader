package com.bn.ninjatrader.process.request;

import com.bn.ninjatrader.common.data.Stock;
import com.bn.ninjatrader.common.type.TimeFrame;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Brad on 7/28/16.
 */
public class CalcRequest {

  private String symbol;
  private Set<String> symbolSet = Sets.newHashSet();
  private LocalDate fromDate;
  private LocalDate toDate;
  private final List<Integer> periods = Lists.newArrayList();
  private final List<TimeFrame> timeFrames = Lists.newArrayList();

  public static CalcRequest calcSymbol(String symbol) {
    return new CalcRequest().symbol(symbol);
  }

  public static CalcRequest calcSymbol(String symbol, String ... moreSymbols) {
    return new CalcRequest().symbols(symbol, moreSymbols);
  }

  public static CalcRequest forStock(Stock stock) {
    checkNotNull(stock, "stock must not be null");
    return new CalcRequest().symbol(stock.getSymbol());
  }

  private CalcRequest() {
    allTimeFrames();
  }

  public CalcRequest symbol(String symbol) {
    this.symbol = symbol;
    symbolSet.clear();
    symbolSet.add(symbol);
    return this;
  }

  public CalcRequest symbols(String symbol, String ... moreSymbols) {
    this.symbol = symbol;
    symbolSet.clear();
    symbolSet.add(symbol);
    symbolSet.addAll(Arrays.asList(moreSymbols));
    return this;
  }

  public CalcRequest from(LocalDate fromDate) {
    this.fromDate = fromDate;
    return this;
  }

  public CalcRequest to(LocalDate toDate) {
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

  public CalcRequest timeFrames(TimeFrame timeFrame) {
    timeFrames.clear();
    timeFrames.add(timeFrame);
    return this;
  }

  public CalcRequest timeFrames(TimeFrame timeFrame, TimeFrame ... moreTimeFrames) {
    timeFrames.clear();
    timeFrames.add(timeFrame);
    timeFrames.addAll(Arrays.asList(moreTimeFrames));
    return this;
  }

  public CalcRequest allTimeFrames() {
    timeFrames.clear();
    timeFrames.addAll(Arrays.asList(TimeFrame.values()));
    return this;
  }

  public String getSymbol() {
    return symbol;
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

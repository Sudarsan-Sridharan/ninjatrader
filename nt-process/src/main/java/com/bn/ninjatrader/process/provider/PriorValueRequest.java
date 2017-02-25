//package com.bn.ninjatrader.process.provider;
//
//import com.bn.ninjatrader.common.type.TimeFrame;
//import com.bn.ninjatrader.model.mongo.dao.ValueDao;
//import com.google.common.base.MoreObjects;
//import com.google.common.base.Objects;
//import com.google.common.collect.Lists;
//
//import java.time.LocalDate;
//import java.util.Collection;
//import java.util.List;
//
///**
// * @author bradwee2000@gmail.com
// */
//public class PriorValueRequest {
//
//  public static final Builder builder() {
//    return new Builder();
//  }
//
//  private final ValueDao valueDao;
//  private final String symbol;
//  private final TimeFrame timeFrame;
//  private final LocalDate priorDate;
//  private final List<Integer> periods = Lists.newArrayList();
//
//  private PriorValueRequest(final ValueDao valueDao,
//                            final String symbol,
//                            final TimeFrame timeFrame,
//                            final LocalDate priorDate,
//                            final List<Integer> periods) {
//    this.valueDao = valueDao;
//    this.symbol = symbol;
//    this.timeFrame = timeFrame;
//    this.priorDate = priorDate;
//    this.periods.addAll(periods);
//  }
//
//  public ValueDao getValueDao() {
//    return valueDao;
//  }
//
//  public String getSymbol() {
//    return symbol;
//  }
//
//  public TimeFrame getTimeFrame() {
//    return timeFrame;
//  }
//
//  public LocalDate getPriorDate() {
//    return priorDate;
//  }
//
//  public List<Integer> getPeriods() {
//    return periods;
//  }
//
//  @Override
//  public boolean equals(Object obj) {
//    if (obj == null || !(obj instanceof PriorValueRequest)) {
//      return false;
//    }
//    if (obj == this) {
//      return true;
//    }
//    final PriorValueRequest rhs = (PriorValueRequest) obj;
//    return Objects.equal(valueDao, rhs.valueDao)
//        && Objects.equal(symbol, rhs.symbol)
//        && Objects.equal(timeFrame, rhs.timeFrame)
//        && Objects.equal(priorDate, rhs.priorDate)
//        && Objects.equal(periods, rhs.periods);
//  }
//
//  @Override
//  public int hashCode() {
//    return Objects.hashCode(valueDao, symbol, timeFrame, priorDate, periods);
//  }
//
//  @Override
//  public String toString() {
//    return MoreObjects.toStringHelper(this)
//        .add("symbol", symbol).add("timeFrame", timeFrame).add("priorDate", priorDate).add("periods", periods)
//        .toString();
//  }
//
//  /**
//   * Builder class
//   */
//  public static final class Builder {
//    private ValueDao valueDao;
//    private String symbol;
//    private TimeFrame timeFrame;
//    private LocalDate priorDate;
//    private List<Integer> periods = Lists.newArrayList();
//
//    public Builder dao(final ValueDao valueDao) {
//      this.valueDao = valueDao;
//      return this;
//    }
//
//    public Builder symbol(final String symbol) {
//      this.symbol = symbol;
//      return this;
//    }
//
//    public Builder timeFrame(final TimeFrame timeFrame) {
//      this.timeFrame = timeFrame;
//      return this;
//    }
//
//    public Builder priorDate(final LocalDate priorDate) {
//      this.priorDate = priorDate;
//      return this;
//    }
//
//    public Builder addPeriod(final int period) {
//      this.periods.add(period);
//      return this;
//    }
//
//    public Builder addPeriods(final Collection<Integer> periods) {
//      if (periods != null) {
//        this.periods.addAll(periods);
//      }
//      return this;
//    }
//
//    public Builder addPeriods(final Integer period, final Integer ... more) {
//      this.periods.addAll(Lists.asList(period, more));
//      return this;
//    }
//
//    public PriorValueRequest build() {
//      return new PriorValueRequest(valueDao, symbol, timeFrame, priorDate, periods);
//    }
//  }
//}

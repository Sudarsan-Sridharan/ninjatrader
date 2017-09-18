package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.common.util.FixedList;
import com.bn.ninjatrader.simulation.data.BarData;
import com.google.common.base.MoreObjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Optional;

import static com.bn.ninjatrader.common.util.FixedList.TrimDirection.RIGHT_TO_LEFT;

/**
 * Created by Brad on 8/22/16.
 */
public class History implements Serializable {
  private static final Logger LOG = LoggerFactory.getLogger(History.class);

  public static History withMaxSize(int maxSize) {
    return new History(maxSize);
  }

  private final FixedList<BarData> barDataList;

  private History() {
    this.barDataList = null;
  }

  private History(final int maxSize) {
    this.barDataList = FixedList.withMaxSizeAndTrimDirection(maxSize, RIGHT_TO_LEFT);
  }

  public void add(final BarData barData) {
    barDataList.add(0, barData);
  }

  public Optional<BarData> getNBarsAgo(int numOfBarsAgo) {
    try {
      return Optional.of(barDataList.get(numOfBarsAgo));
    } catch (IndexOutOfBoundsException e) {
      return Optional.empty();
    }
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).toString();
  }
}

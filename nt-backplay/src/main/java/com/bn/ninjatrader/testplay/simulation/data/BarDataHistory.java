package com.bn.ninjatrader.testplay.simulation.data;

import com.bn.ninjatrader.common.util.FixedList;
import com.google.common.base.Optional;

import java.util.List;

import static com.bn.ninjatrader.common.util.FixedList.TrimDirection.RIGHT_TO_LEFT;

/**
 * Created by Brad on 8/22/16.
 */
public class BarDataHistory {

  public static BarDataHistory withMaxSize(int maxSize) {
    return new BarDataHistory(maxSize);
  }

  private final List<BarData> barDataList;

  private BarDataHistory(int maxSize) {
    this.barDataList = FixedList.withMaxSizeAndTrimDirection(maxSize, RIGHT_TO_LEFT);
  }

  public void add(BarData barData) {
    barDataList.add(0, barData);
  }

  public Optional<BarData> getBarDataDaysAgo(int daysAgo) {
    try {
      int index = daysAgo - 1;
      return Optional.of(barDataList.get(index));
    } catch (IndexOutOfBoundsException e) {
      return Optional.absent();
    }
  }
}

package com.bn.ninjatrader.simulation.data;

import com.bn.ninjatrader.common.util.FixedList;

import java.util.List;
import java.util.Optional;

import static com.bn.ninjatrader.common.util.FixedList.TrimDirection.RIGHT_TO_LEFT;

/**
 * Created by Brad on 8/22/16.
 */
public class History {

  public static History withMaxSize(int maxSize) {
    return new History(maxSize);
  }

  private final List<BarData> barDataList;

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
}

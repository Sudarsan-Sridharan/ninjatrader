package com.bn.ninjatrader.testplay.simulation.data;

import com.bn.ninjatrader.common.data.Price;
import com.google.common.base.Optional;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Created by Brad on 8/22/16.
 */
public class BarDataHistoryTest {

  private final LocalDate date1 = LocalDate.of(2016, 1, 1);
  private final LocalDate date2 = LocalDate.of(2016, 1, 2);
  private final LocalDate date3 = LocalDate.of(2016, 1, 3);
  private final LocalDate date4 = LocalDate.of(2016, 1, 4);

  private Price price1 = new Price(date1, 1.1, 1.2, 1.3, 1.4, 10000);
  private Price price2 = new Price(date2, 2.1, 2.2, 2.3, 2.4, 20000);
  private Price price3 = new Price(date3, 3.1, 3.2, 3.3, 3.4, 30000);
  private Price price4 = new Price(date4, 4.1, 4.2, 4.3, 4.4, 40000);

  @Test
  public void testCreateEmpty() {
    BarDataHistory barDataHistory = BarDataHistory.withMaxSize(52);

    Optional<BarData> foundBarData = barDataHistory.getBarDataDaysAgo(0);
    assertFalse(foundBarData.isPresent());

    foundBarData = barDataHistory.getBarDataDaysAgo(1);
    assertFalse(foundBarData.isPresent());

    foundBarData = barDataHistory.getBarDataDaysAgo(-100);
    assertFalse(foundBarData.isPresent());

    foundBarData = barDataHistory.getBarDataDaysAgo(100);
    assertFalse(foundBarData.isPresent());
  }

  @Test
  public void testWithData() {
    BarDataHistory barDataHistory = BarDataHistory.withMaxSize(3);

    barDataHistory.add(new BarData().put(price1));
    assertBarDataPriceEqualsPrice(barDataHistory.getBarDataDaysAgo(1), price1);
    assertBarDataNotExist(barDataHistory.getBarDataDaysAgo(2));

    barDataHistory.add(new BarData().put(price2));
    assertBarDataPriceEqualsPrice(barDataHistory.getBarDataDaysAgo(1), price2);
    assertBarDataPriceEqualsPrice(barDataHistory.getBarDataDaysAgo(2), price1);
  }

  @Test
  public void testWithDataExceedingMaxSize() {
    BarDataHistory barDataHistory = BarDataHistory.withMaxSize(3);
    barDataHistory.add(new BarData().put(price1));
    barDataHistory.add(new BarData().put(price2));
    barDataHistory.add(new BarData().put(price3));
    barDataHistory.add(new BarData().put(price4));

    assertBarDataPriceEqualsPrice(barDataHistory.getBarDataDaysAgo(1), price4);
    assertBarDataPriceEqualsPrice(barDataHistory.getBarDataDaysAgo(2), price3);
    assertBarDataPriceEqualsPrice(barDataHistory.getBarDataDaysAgo(3), price2);

    assertBarDataNotExist(barDataHistory.getBarDataDaysAgo(4));
  }

  private void assertBarDataPriceEqualsPrice(Optional<BarData> foundBarData, Price price) {
    assertTrue(foundBarData.isPresent());
    assertEquals(foundBarData.get().getPrice(), price);
  }

  private void assertBarDataNotExist(Optional<BarData> foundBarData) {
    assertFalse(foundBarData.isPresent());
  }
}

package com.bn.ninjatrader.simulation.data;

import com.bn.ninjatrader.common.data.Price;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
  public void testCreateWithNoHistory_shouldReturnEmpty() {
    BarDataHistory history = BarDataHistory.withMaxSize(52);

    Optional<BarData> foundBarData = history.getNBarsAgo(0);
    assertThat(foundBarData).isEmpty();

    foundBarData = history.getNBarsAgo(1);
    assertThat(foundBarData).isEmpty();

    foundBarData = history.getNBarsAgo(-100);
    assertThat(foundBarData).isEmpty();

    foundBarData = history.getNBarsAgo(100);
    assertThat(foundBarData).isEmpty();
  }

  @Test
  public void testWithHistoryData_shouldReturnHistoryData() {
    final BarDataHistory history = BarDataHistory.withMaxSize(3);

    history.add(BarData.builder().price(price1).build());
    assertThat(history.getNBarsAgo(0)).isPresent();
    assertThat(history.getNBarsAgo(0).get().getPrice()).isEqualTo(price1);
    assertThat(history.getNBarsAgo(2)).isEmpty();

    history.add(BarData.builder().price(price2).build());
    assertThat(history.getNBarsAgo(0)).isPresent();
    assertThat(history.getNBarsAgo(0).get().getPrice()).isEqualTo(price2);
    assertThat(history.getNBarsAgo(1)).isPresent();
    assertThat(history.getNBarsAgo(1).get().getPrice()).isEqualTo(price1);
  }

  @Test
  public void testWithHistoryDataExceedingMaxSize_shouldKeepHistoryToMaxSize() {
    final BarDataHistory history = BarDataHistory.withMaxSize(3);
    history.add(BarData.builder().price(price1).build());
    history.add(BarData.builder().price(price2).build());
    history.add(BarData.builder().price(price3).build());
    history.add(BarData.builder().price(price4).build());

    assertThat(history.getNBarsAgo(0)).isPresent();
    assertThat(history.getNBarsAgo(1)).isPresent();
    assertThat(history.getNBarsAgo(2)).isPresent();

    assertThat(history.getNBarsAgo(3)).isEmpty();
  }
}

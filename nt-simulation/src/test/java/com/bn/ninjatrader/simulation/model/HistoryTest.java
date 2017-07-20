package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.simulation.data.BarData;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 8/22/16.
 */
public class HistoryTest {

  private final LocalDate date1 = LocalDate.of(2016, 1, 1);
  private final LocalDate date2 = LocalDate.of(2016, 1, 2);
  private final LocalDate date3 = LocalDate.of(2016, 1, 3);
  private final LocalDate date4 = LocalDate.of(2016, 1, 4);

  private Price price1 = Price.builder().date(date1).open(1.1).high(1.2).low(1.3).close(1.4).volume(10000).build();
  private Price price2 = Price.builder().date(date2).open(2.1).high(2.2).low(2.3).close(2.4).volume(20000).build();
  private Price price3 = Price.builder().date(date3).open(3.1).high(3.2).low(3.3).close(3.4).volume(30000).build();
  private Price price4 = Price.builder().date(date4).open(4.1).high(4.2).low(4.3).close(4.4).volume(40000).build();

  @Test
  public void testCreateWithNoHistory_shouldReturnEmpty() {
    History history = History.withMaxSize(52);

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
    final History history = History.withMaxSize(3);

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
    final History history = History.withMaxSize(3);
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

package com.bn.ninjatrader.calculator;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.model.entity.Price;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 5/27/16.
 */
public class WeeklyPriceCalculatorTest {

  private final LocalDate now = LocalDate.of(2016, 6, 6);

  private WeeklyPriceCalculator calculator;

  @Before
  public void before() {
    calculator = new WeeklyPriceCalculator();
  }

  @Test
  public void testCalcWithOneWeekPrices_shouldMergePricesToWeeklyPrice() {
    final Price mon = Price.builder()
        .date(now).open(1).high(1.1).low(1.2).close(1.3).volume(10000).build();
    final Price tue = Price.builder()
        .date(now.plusDays(1)).open(2).high(2.1).low(2.2).close(2.3).volume(20000).build();
    final Price wed = Price.builder()
        .date(now.plusDays(2)).open(3).high(3.1).low(3.2).close(3.3).volume(30000).build();
    final Price thr = Price.builder()
        .date(now.plusDays(3)).open(4).high(4.1).low(4.2).close(4.3).volume(40000).build();
    final Price fri = Price.builder()
        .date(now.plusDays(4)).open(5).high(5.1).low(5.2).close(5.3).volume(50000).build();

    assertThat(calculator.calc(mon, tue, wed, thr, fri))
        .containsExactly(Price.builder()
            .date(now).open(1.0).high(5.1).low(1.2).close(5.3).volume(150000).build());
  }

  @Test
  public void testCalcWithMultipleWeekPrices_shouldReturnPriceForEachWeek() {
    // Week 1
    final LocalDate date1 = LocalDate.of(2016, 6, 6);
    final Price monPrice1 = Price.builder()
        .date(date1).open(1).high(1.1).low(1.2).close(1.3).volume(1000).build();
    final Price tuePrice1 = Price.builder().date(date1.plusDays(1))
        .open(2).high(2.1).low(2.2).close(2.3).volume(2000).build();

    // Week 2
    final LocalDate date2 = LocalDate.of(2016, 6, 13);
    final Price wedPrice2 = Price.builder().date(date2.plusDays(2))
        .open(3).high(3.1).low(3.2).close(3.3).volume(3000).build();
    final Price thrPrice2 = Price.builder().date(date2.plusDays(3))
        .open(4).high(4.1).low(4.2).close(4.3).volume(4000).build();

    // Week 3
    final LocalDate date3 = LocalDate.of(2016, 6, 20);
    final Price monPrice3 = Price.builder()
        .date(date3).open(5).high(5.1).low(5.2).close(5.3).volume(5000).build();
    final Price friPrice3 = Price.builder().date(date3.plusDays(4))
        .open(6).high(6.1).low(6.2).close(6.3).volume(5000).build();

    // Verify weekly prices
    assertThat(calculator.calc(monPrice1, tuePrice1, wedPrice2, thrPrice2, monPrice3, friPrice3))
        .containsExactly(
            Price.builder().date(date1).open(1.0).high(2.1).low(1.2).close(2.3).volume(3000).build(),
            Price.builder().date(date2).open(3.0).high(4.1).low(3.2).close(4.3).volume(7000).build(),
            Price.builder().date(date3).open(5).high(6.1).low(5.2).close(6.3).volume(10000).build()
    );
  }

  @Test
  public void testCalcWithPricesInSameWeekButDifferentYears_shouldReturnAsSeparateWeeks() {
    final LocalDate now = LocalDate.of(2015, 1, 5);
    final LocalDate nextYear = LocalDate.of(2016, 1, 11);

    final Price price1 = Price.builder().date(now).open(1).high(2).low(3).close(4).volume(10000).build();
    final Price price2 = Price.builder().date(nextYear).open(1).high(2).low(3).close(4).volume(10000).build();

    final List<Price> result = calculator.calc(Lists.newArrayList(price1, price2));

    assertThat(result).containsExactly(price1, price2);
  }
}

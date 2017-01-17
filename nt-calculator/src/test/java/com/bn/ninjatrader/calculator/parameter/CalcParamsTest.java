package com.bn.ninjatrader.calculator.parameter;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.util.TestUtil;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 9/6/16.
 */
public class CalcParamsTest {

  private final Price price = TestUtil.randomPrice();
  private final List<Price> prices = Lists.newArrayList(price);

  private final LocalDate date1 = LocalDate.of(2016, 2, 2);
  private final LocalDate date2 = LocalDate.of(2016, 2, 3);

  private final Value value1 = Value.of(date1, 1);
  private final Value value2 = Value.of(date2, 2);

  @Test
  public void testWithNullPrices_shouldNotThrowNullPointerException() {
    final CalcParams params = CalcParams.withPrices(null);
    assertThat(params).isNotNull();
    assertThat(params.getPrices()).isNotNull().isEmpty();
  }

  @Test
  public void testWithPricesAndPeriods_shouldAddAllPricesAndPeriods() {
    final CalcParams params = CalcParams.withPrices(prices).periods(20, 50, 100);
    assertThat(params.getPrices()).isEqualTo(prices);
    assertThat(params.getPeriods()).containsExactly(20, 50, 100);
  }

  @Test
  public void testAddPriorValues_shouldAddPriorValues() {
    final CalcParams params = CalcParams.withPrices(prices).addPriorValue(20, value1).addPriorValue(50, value2);
    assertThat(params.getPriorValueForPeriod(20)).hasValue(value1);
    assertThat(params.getPriorValueForPeriod(50)).hasValue(value2);
    assertThat(params.getPriorValueForPeriod(400)).isNotPresent();
  }
}

package com.bn.ninjatrader.simulation.data;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.entity.PriceBuilderFactory;
import com.bn.ninjatrader.model.util.DummyPriceBuilderFactory;
import com.bn.ninjatrader.simulation.calculator.VarCalculator;
import com.bn.ninjatrader.simulation.model.World;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Collections;

import static com.bn.ninjatrader.simulation.logicexpression.Variables.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Brad on 8/24/16.
 */
public class BarDataFactoryTest {

  private final LocalDate now = LocalDate.of(2016, 1, 1);
  private final PriceBuilderFactory pbf = new DummyPriceBuilderFactory();
  private final Price price = pbf.builder().date(now).close(1).build();

  private BarDataFactory barDataFactory;
  private World world;

  @Before
  public void setup() {
    world = mock(World.class);

    barDataFactory = new BarDataFactory();
  }

  @Test
  public void testCreateBarData_shouldSetProperties() {
    final BarData barData = barDataFactory.create("MEG", price, 1, null, Collections.emptyList());

    assertThat(barData).isNotNull();
    assertThat(barData.getPrice()).isEqualTo(price);
    assertThat(barData.getIndex()).isEqualTo(1);
    assertThat(barData.getSymbol()).isEqualTo("MEG");
  }

  @Test
  public void testWithMultiVarCalculators_shouldFillAllVariablesWithValues() {
    final VarCalculator varCalculator1 = mock(VarCalculator.class);
    final VarCalculator varCalculator2 = mock(VarCalculator.class);

    when(varCalculator1.calc(any(Price.class))).thenReturn(DataMap.newInstance()
        .addData(PRICE_OPEN, 1.0).addData(PRICE_HIGH, 2.0));
    when(varCalculator2.calc(any(Price.class))).thenReturn(DataMap.newInstance()
        .addData(SMA.withPeriod(21), 100.15));

    final BarData barData =
        barDataFactory.create("MEG", price, 0, world, Lists.newArrayList(varCalculator1, varCalculator2));

    assertThat(barData.get(SMA.withPeriod(21))).isEqualTo(100.15);
    assertThat(barData.get(PRICE_OPEN)).isEqualTo(1.0);
    assertThat(barData.get(PRICE_HIGH)).isEqualTo(2.0);
  }
}

package com.bn.ninjatrader.simulation.data;

import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.entity.PriceBuilderFactory;
import com.bn.ninjatrader.model.util.DummyPriceBuilderFactory;
import com.bn.ninjatrader.simulation.binding.BindingProvider;
import com.bn.ninjatrader.simulation.model.SimContext;
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
public class BarProducerTest {

  private final LocalDate now = LocalDate.of(2016, 1, 1);
  private final PriceBuilderFactory pbf = new DummyPriceBuilderFactory();
  private final Price price = pbf.builder().date(now).close(1).build();


  private BindingProvider bindingProvider;
  private BindingProvider bindingProvider2;
  private DataMap dataMap;
  private BarProducer barProducer;
  private SimContext simContext;

  @Before
  public void setup() {
    bindingProvider = mock(BindingProvider.class);
    bindingProvider2 = mock(BindingProvider.class);
    dataMap = mock(DataMap.class);
    simContext = mock(SimContext.class);

    barProducer = new BarProducer(Lists.newArrayList(bindingProvider, bindingProvider2));

    when(bindingProvider.get(any())).thenReturn(dataMap);
    when(bindingProvider2.get(any())).thenReturn(dataMap);
    when(dataMap.toMap()).thenReturn(Collections.emptyMap());
  }

  @Test
  public void testCreateBarData_shouldSetProperties() {
    final BarData barData = barProducer.nextBar("MEG", price, simContext);

    assertThat(barData).isNotNull();
    assertThat(barData.getPrice()).isEqualTo(price);
    assertThat(barData.getIndex()).isEqualTo(1);
    assertThat(barData.getSymbol()).isEqualTo("MEG");
  }

  @Test
  public void testWithMultiVarCalculators_shouldFillAllVariablesWithValues() {
    when(bindingProvider.get(any(Price.class))).thenReturn(DataMap.newInstance()
        .addData(PRICE_OPEN, 1.0).addData(PRICE_HIGH, 2.0));
    when(bindingProvider2.get(any(Price.class))).thenReturn(DataMap.newInstance()
        .addData(SMA.withPeriod(21), 100.15));

    final BarData barData =
        barProducer.nextBar("MEG", price, simContext);

    assertThat(barData.get(SMA.withPeriod(21))).isEqualTo(100.15);
    assertThat(barData.get(PRICE_OPEN)).isEqualTo(1.0);
    assertThat(barData.get(PRICE_HIGH)).isEqualTo(2.0);
  }
}

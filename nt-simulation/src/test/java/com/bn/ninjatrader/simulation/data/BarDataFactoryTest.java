package com.bn.ninjatrader.simulation.data;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.simulation.adaptor.PriceDataMapAdaptor;
import com.bn.ninjatrader.simulation.core.SimulationData;
import com.bn.ninjatrader.simulation.model.History;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static com.bn.ninjatrader.simulation.operation.Variables.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Brad on 8/24/16.
 */
public class BarDataFactoryTest {

  private final LocalDate now = LocalDate.of(2016, 1, 1);
  private final Price price = Price.builder().date(now).close(1).build();
  private final DataMap dataMap = DataMap.newInstance().addData(PRICE_OPEN, 1.0).addData(PRICE_HIGH, 2.0);

  private PriceDataMapAdaptor priceDataMapAdaptor;
  private BarDataFactory barDataFactory;
  private History history;

  @Before
  public void setup() {
    priceDataMapAdaptor = mock(PriceDataMapAdaptor.class);
    history = mock(History.class);

    when(priceDataMapAdaptor.toDataMap(any(Price.class))).thenReturn(dataMap);

    barDataFactory = new BarDataFactory(priceDataMapAdaptor);
  }

  @Test
  public void testCreateBarData_shouldSetProperties() {
    final BarData barData = barDataFactory.createWithPriceAtIndex(price, 1);

    assertThat(barData).isNotNull();
    assertThat(barData.getPrice()).isEqualTo(price);
    assertThat(barData.getIndex()).isEqualTo(1);
    assertThat(barData.get(PRICE_OPEN)).isEqualTo(1.0);
    assertThat(barData.get(PRICE_HIGH)).isEqualTo(2.0);
    assertThat(barData.get(BAR_INDEX)).isEqualTo(1.0);
  }

  @Test
  public void testCreateBarDataWithSimulationData_shouldFillDataMapVariablesWithValues() {
    final SimulationData<Value> simulationData = mock(SimulationData.class);
    final DataMap dataMap = DataMap.newInstance().addData(SMA.withPeriod(21), 100.15);

    when(simulationData.getDataAtIndex(0)).thenReturn(dataMap);

    final BarData barData =
        barDataFactory.createWithPriceAtIndex(price, 0, Lists.newArrayList(simulationData), history);
    assertThat(barData.get(SMA.withPeriod(21))).isEqualTo(100.15);
  }
}

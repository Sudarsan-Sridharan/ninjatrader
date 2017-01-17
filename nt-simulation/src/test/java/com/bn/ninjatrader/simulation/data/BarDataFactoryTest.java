package com.bn.ninjatrader.simulation.data;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.simulation.operation.Variable;
import mockit.Expectations;
import mockit.Mocked;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static com.bn.ninjatrader.simulation.data.DataType.SMA;
import static com.bn.ninjatrader.simulation.operation.Variables.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 8/24/16.
 */
public class BarDataFactoryTest {

  private BarDataFactory barDataFactory;

  private final LocalDate now = LocalDate.of(2016, 1, 1);
  private final Price price1 = new Price(now, 1, 2, 3, 4, 10000);

  @BeforeMethod
  public void setup() {
    barDataFactory = new BarDataFactory();
  }

  @Test
  public void testCreateBarData() {
    BarData barData = barDataFactory.create(price1);

    assertThat(barData).isNotNull();
    assertThatBarDataPriceEqualsPrice(barData, price1);
  }

  @Test
  public void testCreateBarDataWithSimulationData(@Mocked SimulationData<Value> simulationData) {
    new Expectations() {{
      DataMap dataMap = new DataMap();
      dataMap.put(Variable.of(SMA).period(21), 100.15);

      simulationData.getDataMap(0);
      result = dataMap;
    }};
    barDataFactory.addSimulationData(Lists.newArrayList(simulationData));

    BarData barData = barDataFactory.create(price1);
    assertThat(barData.get(BAR_INDEX)).isEqualTo(0.0);
    assertThat(barData.get(Variable.of(SMA).period(21))).isEqualTo(100.15);
    assertThatBarDataPriceEqualsPrice(barData, price1);
  }

  private void assertThatBarDataPriceEqualsPrice(BarData barData, Price price) {
    assertThat(barData.getPrice()).isEqualTo(price1);
    assertThat(barData.get(PRICE_OPEN)).isEqualTo(price.getOpen());
    assertThat(barData.get(PRICE_HIGH)).isEqualTo(price.getHigh());
    assertThat(barData.get(PRICE_LOW)).isEqualTo(price.getLow());
    assertThat(barData.get(PRICE_CLOSE)).isEqualTo(price.getClose());
    assertThat(barData.get(VOLUME)).isEqualTo((double) price.getVolume());
  }
}

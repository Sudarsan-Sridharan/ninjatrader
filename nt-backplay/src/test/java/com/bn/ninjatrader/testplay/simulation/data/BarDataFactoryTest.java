package com.bn.ninjatrader.testplay.simulation.data;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.data.Value;
import mockit.Expectations;
import mockit.Mocked;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static com.bn.ninjatrader.testplay.simulation.data.DataType.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created by Brad on 8/24/16.
 */
public class BarDataFactoryTest {

  private BarDataFactory barDataFactory;

  private LocalDate now = LocalDate.of(2016, 1, 1);

  private final Price price1 = new Price(now, 1, 2, 3, 4, 10000);

  @BeforeMethod
  public void setup() {
    barDataFactory = new BarDataFactory();
  }

  @Test
  public void testCreateBarData() {
    BarData barData = barDataFactory.create(price1);

    assertNotNull(barData);
    assertBarDataPriceEqualsPrice(barData, price1);
  }

  @Test
  public void testCreateBarDataWithSimulationData(@Mocked SimulationData<Value> simulationData) {
    new Expectations() {{
      DataMap dataMap = new DataMap();
      dataMap.put(SMA_21, 100.15);

      simulationData.getDataMap(0);
      result = dataMap;
    }};
    barDataFactory.addSimulationData(Lists.newArrayList(simulationData));

    BarData barData = barDataFactory.create(price1);
    assertEquals(barData.get(BAR_INDEX), 0.0);
    assertEquals(barData.get(SMA_21), 100.15);
    assertBarDataPriceEqualsPrice(barData, price1);
  }

  private void assertBarDataPriceEqualsPrice(BarData barData, Price price) {
    assertEquals(barData.getPrice(), price1);
    assertEquals(barData.get(PRICE_OPEN), price.getOpen());
    assertEquals(barData.get(PRICE_HIGH), price.getHigh());
    assertEquals(barData.get(PRICE_LOW), price.getLow());
    assertEquals(barData.get(PRICE_CLOSE), price.getClose());
    assertEquals(barData.get(VOLUME), (double) price.getVolume());
  }
}

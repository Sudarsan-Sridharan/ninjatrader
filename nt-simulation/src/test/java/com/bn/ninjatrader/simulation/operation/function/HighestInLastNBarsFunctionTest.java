package com.bn.ninjatrader.simulation.operation.function;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.data.BarDataFactory;
import com.bn.ninjatrader.simulation.operation.Operation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.bn.ninjatrader.simulation.operation.Variables.PRICE_CLOSE;
import static com.bn.ninjatrader.simulation.operation.Variables.PRICE_OPEN;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 8/29/16.
 */
public class HighestInLastNBarsFunctionTest {

  private final ObjectMapper om = TestUtil.objectMapper();
  private final Price price1 = new Price(null, 1.1, 1.2, 1.3, 1.4, 10000);
  private final Price price2 = new Price(null, 2.1, 2.2, 2.3, 2.4, 20000);
  private final Price price3 = new Price(null, 3.1, 3.2, 3.3, 3.4, 30000);

  private BarDataFactory barDataFactory;

  @BeforeMethod
  public void setup() {
    barDataFactory = new BarDataFactory();
  }

  @Test
  public void testValueOfSingleHistoryBar_shouldReturnValueOfOneDayAgo() {
    barDataFactory.create(price1);
    BarData barData = barDataFactory.create(price1);

    assertThat(HighestInNBarsAgoFunction.of(PRICE_CLOSE).withinBarsAgo(0).getValue(barData)).isEqualTo(0.0);
    assertThat(HighestInNBarsAgoFunction.of(PRICE_CLOSE).withinBarsAgo(1).getValue(barData)).isEqualTo(1.4);
    assertThat(HighestInNBarsAgoFunction.of(PRICE_CLOSE).withinBarsAgo(100).getValue(barData)).isEqualTo(1.4);
  }

  @Test
  public void testValueOfMultipleHistoryBars_shouldReturnHighestValueOfNDaysAgo() {
    barDataFactory.create(price1); // Two bars ago
    BarData barData = barDataFactory.create(price2); // One bars ago

    assertThat(HighestInNBarsAgoFunction.of(PRICE_CLOSE).withinBarsAgo(1).getValue(barData)).isEqualTo(1.4);

    barData = barDataFactory.create(price3);

    assertThat(HighestInNBarsAgoFunction.of(PRICE_CLOSE).withinBarsAgo(1).getValue(barData)).isEqualTo(2.4);
    assertThat(HighestInNBarsAgoFunction.of(PRICE_CLOSE).withinBarsAgo(2).getValue(barData)).isEqualTo(2.4);
    assertThat(HighestInNBarsAgoFunction.of(PRICE_OPEN).withinBarsAgo(2).getValue(barData)).isEqualTo(2.1);
  }

  @Test
  public void testSerializeDeserialize_shouldReturnSameObject() throws IOException {
    HighestInNBarsAgoFunction function = new HighestInNBarsAgoFunction(PRICE_CLOSE, 1);
    String serialized = om.writeValueAsString(function);
    Operation deserialized = om.readValue(serialized, Operation.class);

    assertThat(deserialized).isInstanceOf(HighestInNBarsAgoFunction.class);

    HighestInNBarsAgoFunction deserializedFunction = (HighestInNBarsAgoFunction) deserialized;
    assertThat(deserializedFunction.getNumOfBarsAgo()).isEqualTo(1);
    assertThat(deserializedFunction.getOperation()).isEqualTo(PRICE_CLOSE);
  }
}

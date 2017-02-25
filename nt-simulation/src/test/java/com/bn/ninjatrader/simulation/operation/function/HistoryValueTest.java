package com.bn.ninjatrader.simulation.operation.function;

import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.History;
import com.bn.ninjatrader.simulation.model.World;
import com.bn.ninjatrader.simulation.util.DummyObjectMapperProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static com.bn.ninjatrader.simulation.operation.Variables.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Brad on 8/29/16.
 */
public class HistoryValueTest {
  private BarData barData1;
  private BarData barData2;
  private BarData barData3;

  private History history;
  private World world;

  @Before
  public void before() {
    world = mock(World.class);
    history = mock(History.class);

    barData1 = BarData.builder().addData(PRICE_CLOSE, 1.4).world(world).build();
    barData2 = BarData.builder().addData(PRICE_CLOSE, 2.4).world(world).build();
    barData3 = BarData.builder().addData(PRICE_CLOSE, 3.4).world(world).build();

    when(world.getHistory()).thenReturn(history);

    when(history.getNBarsAgo(1)).thenReturn(Optional.of(barData1));
    when(history.getNBarsAgo(2)).thenReturn(Optional.of(barData2));
    when(history.getNBarsAgo(3)).thenReturn(Optional.of(barData3));
  }

  @Test
  public void testGetValue_shouldReturnValueUsingBarDataOfGivenNBarsAgo() {
    assertThat(HistoryValue.of(PRICE_CLOSE).inNumOfBarsAgo(1).getValue(barData1)).isEqualTo(1.4);
    assertThat(HistoryValue.of(PRICE_CLOSE).inNumOfBarsAgo(2).getValue(barData1)).isEqualTo(2.4);
    assertThat(HistoryValue.of(PRICE_CLOSE).inNumOfBarsAgo(3).getValue(barData1)).isEqualTo(3.4);
  }

  @Test
  public void testGetValueWithNBarsAgoExceedingHistory_shouldReturnNaN() {
    assertThat(HistoryValue.of(PRICE_CLOSE).inNumOfBarsAgo(100).getValue(barData1)).isEqualTo(Double.NaN);
  }

  @Test
  public void testSerializeDeserialize_shouldProduceEqualObject() throws IOException {
    final ObjectMapper om = DummyObjectMapperProvider.get();
    final HistoryValue historyValue = HistoryValue.of(PRICE_CLOSE).inNumOfBarsAgo(100);
    final String json = om.writeValueAsString(historyValue);
    assertThat(om.readValue(json, Operation.class)).isEqualTo(historyValue);
  }

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    final HistoryValue historyValue = HistoryValue.of(PRICE_CLOSE).inNumOfBarsAgo(100);

    assertThat(historyValue).isEqualTo(historyValue)
        .isEqualTo(HistoryValue.of(PRICE_CLOSE).inNumOfBarsAgo(100))
        .isNotEqualTo(null)
        .isNotEqualTo("")
        .isNotEqualTo(HistoryValue.of(PRICE_CLOSE).inNumOfBarsAgo(101))
        .isNotEqualTo(HistoryValue.of(PRICE_HIGH).inNumOfBarsAgo(100));
  }

  @Test
  public void testHashcode_shouldHaveEqualHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(
        HistoryValue.of(PRICE_CLOSE).inNumOfBarsAgo(100),
        HistoryValue.of(PRICE_CLOSE).inNumOfBarsAgo(100),
        HistoryValue.of(PRICE_CLOSE).inNumOfBarsAgo(101),
        HistoryValue.of(PRICE_OPEN).inNumOfBarsAgo(100)))
        .containsExactlyInAnyOrder(
            HistoryValue.of(PRICE_CLOSE).inNumOfBarsAgo(100),
            HistoryValue.of(PRICE_CLOSE).inNumOfBarsAgo(101),
            HistoryValue.of(PRICE_OPEN).inNumOfBarsAgo(100)
        );
  }
}

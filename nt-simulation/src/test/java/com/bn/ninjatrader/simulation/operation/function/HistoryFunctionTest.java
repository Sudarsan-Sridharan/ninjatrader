package com.bn.ninjatrader.simulation.operation.function;

import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.data.History;
import com.bn.ninjatrader.simulation.operation.Operation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static com.bn.ninjatrader.simulation.operation.Variables.PRICE_CLOSE;
import static com.bn.ninjatrader.simulation.operation.Variables.PRICE_HIGH;
import static com.bn.ninjatrader.simulation.operation.Variables.PRICE_OPEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Brad on 8/29/16.
 */
public class HistoryFunctionTest {
  private BarData barData1;
  private BarData barData2;
  private BarData barData3;

  private History history;

  @Before
  public void before() {
    history = mock(History.class);

    barData1 = BarData.builder().addData(PRICE_CLOSE, 1.4).history(history).build();
    barData2 = BarData.builder().addData(PRICE_CLOSE, 2.4).history(history).build();
    barData3 = BarData.builder().addData(PRICE_CLOSE, 3.4).history(history).build();

    when(history.getNBarsAgo(1)).thenReturn(Optional.of(barData1));
    when(history.getNBarsAgo(2)).thenReturn(Optional.of(barData2));
    when(history.getNBarsAgo(3)).thenReturn(Optional.of(barData3));
  }

  @Test
  public void testGetValue_shouldReturnValueUsingBarDataOfGivenNBarsAgo() {
    assertThat(HistoryFunction.withNBarsAgo(PRICE_CLOSE, 1).getValue(barData1)).isEqualTo(1.4);
    assertThat(HistoryFunction.withNBarsAgo(PRICE_CLOSE, 2).getValue(barData1)).isEqualTo(2.4);
    assertThat(HistoryFunction.withNBarsAgo(PRICE_CLOSE, 3).getValue(barData1)).isEqualTo(3.4);
  }

  @Test
  public void testGetValueWithNBarsAgoExceedingHistory_shouldReturnNaN() {
    assertThat(HistoryFunction.withNBarsAgo(PRICE_CLOSE, 100).getValue(barData1)).isEqualTo(Double.NaN);
  }

  @Test
  public void testSerializeDeserialize_shouldProduceEqualObject() throws IOException {
    final ObjectMapper om = TestUtil.objectMapper();
    final HistoryFunction historyFunction = HistoryFunction.withNBarsAgo(PRICE_CLOSE, 100);
    final String json = om.writeValueAsString(historyFunction);
    assertThat(om.readValue(json, Operation.class)).isEqualTo(historyFunction);
  }

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    final HistoryFunction historyFunction = HistoryFunction.withNBarsAgo(PRICE_CLOSE, 100);

    assertThat(historyFunction).isEqualTo(historyFunction)
        .isEqualTo(HistoryFunction.withNBarsAgo(PRICE_CLOSE, 100))
        .isNotEqualTo(null)
        .isNotEqualTo("")
        .isNotEqualTo(HistoryFunction.withNBarsAgo(PRICE_CLOSE, 101))
        .isNotEqualTo(HistoryFunction.withNBarsAgo(PRICE_HIGH, 100));
  }

  @Test
  public void testHashcode_shouldHaveEqualHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(
        HistoryFunction.withNBarsAgo(PRICE_CLOSE, 100),
        HistoryFunction.withNBarsAgo(PRICE_CLOSE, 100),
        HistoryFunction.withNBarsAgo(PRICE_CLOSE, 101),
        HistoryFunction.withNBarsAgo(PRICE_OPEN, 100)))
        .containsExactlyInAnyOrder(
            HistoryFunction.withNBarsAgo(PRICE_CLOSE, 100),
            HistoryFunction.withNBarsAgo(PRICE_CLOSE, 101),
            HistoryFunction.withNBarsAgo(PRICE_OPEN, 100)
        );
  }
}

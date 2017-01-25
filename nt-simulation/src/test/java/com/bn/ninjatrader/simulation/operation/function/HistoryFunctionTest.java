package com.bn.ninjatrader.simulation.operation.function;

import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.data.History;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static com.bn.ninjatrader.simulation.operation.Variables.PRICE_CLOSE;
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
}

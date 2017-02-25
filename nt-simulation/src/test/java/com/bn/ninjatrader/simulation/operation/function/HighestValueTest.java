package com.bn.ninjatrader.simulation.operation.function;

import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.History;
import com.bn.ninjatrader.simulation.model.World;
import com.bn.ninjatrader.simulation.util.DummyObjectMapperProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static com.bn.ninjatrader.simulation.operation.Variables.PRICE_CLOSE;
import static com.bn.ninjatrader.simulation.operation.Variables.PRICE_OPEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Brad on 8/29/16.
 */
public class HighestValueTest {

  private History history;
  private World world;

  private BarData bar1;
  private BarData bar2;
  private BarData bar3;

  @Before
  public void setup() {
    history = mock(History.class);
    world = mock(World.class);

    bar1 = BarData.builder().world(world).addData(PRICE_CLOSE, 1).addData(PRICE_OPEN, 1.1).build();
    bar2 = BarData.builder().world(world).addData(PRICE_CLOSE, 1.00001).addData(PRICE_OPEN, 2).build();
    bar3 = BarData.builder().world(world).addData(PRICE_CLOSE, 3).addData(PRICE_OPEN, 2.9).build();

    when(world.getHistory()).thenReturn(history);

    when(history.getNBarsAgo(0)).thenReturn(Optional.of(bar1));
    when(history.getNBarsAgo(1)).thenReturn(Optional.of(bar1));
    when(history.getNBarsAgo(2)).thenReturn(Optional.of(bar2));
    when(history.getNBarsAgo(3)).thenReturn(Optional.of(bar3));
  }

  @Test
  public void testGetValue_shouldReturnHighestValueAmongNBars() {
    assertThat(HighestValue.of(PRICE_CLOSE).fromBarsAgo(1).getValue(bar1)).isEqualTo(1.0);
    assertThat(HighestValue.of(PRICE_CLOSE).fromBarsAgo(2).getValue(bar1)).isEqualTo(1.00001);
    assertThat(HighestValue.of(PRICE_CLOSE).fromBarsAgo(3).getValue(bar1)).isEqualTo(3);

    assertThat(HighestValue.of(PRICE_CLOSE).toBarsAgo(1).getValue(bar1)).isEqualTo(1.0);
    assertThat(HighestValue.of(PRICE_CLOSE).toBarsAgo(2).getValue(bar1)).isEqualTo(1.00001);
    assertThat(HighestValue.of(PRICE_CLOSE).toBarsAgo(3).getValue(bar1)).isEqualTo(3);
  }

  @Test
  public void testValueWithBarsAgoRange_shouldReturnLowestValueInRange() {
    assertThat(HighestValue.of(PRICE_CLOSE).fromBarsAgo(1).toBarsAgo(3).getValue(bar1)).isEqualTo(3.0);
    assertThat(HighestValue.of(PRICE_CLOSE).fromBarsAgo(3).toBarsAgo(1).getValue(bar1)).isEqualTo(3.0);

    assertThat(HighestValue.of(PRICE_CLOSE).fromBarsAgo(2).toBarsAgo(3).getValue(bar1)).isEqualTo(3.0);
    assertThat(HighestValue.of(PRICE_CLOSE).fromBarsAgo(3).toBarsAgo(2).getValue(bar1)).isEqualTo(3.0);

    assertThat(HighestValue.of(PRICE_CLOSE).fromBarsAgo(1).toBarsAgo(2).getValue(bar1)).isEqualTo(1.00001);
    assertThat(HighestValue.of(PRICE_CLOSE).fromBarsAgo(2).toBarsAgo(1).getValue(bar1)).isEqualTo(1.00001);
  }

  @Test
  public void testValueWithMultiOperations_shouldReturnLowestValueAmongOperations() {
    assertThat(HighestValue.of(PRICE_CLOSE, PRICE_OPEN).getValue(bar1)).isEqualTo(1.1);
    assertThat(HighestValue.of(PRICE_CLOSE, PRICE_OPEN).fromBarsAgo(2).getValue(bar1)).isEqualTo(2);
    assertThat(HighestValue.of(PRICE_CLOSE, PRICE_OPEN).fromBarsAgo(3).getValue(bar1)).isEqualTo(3);
  }


  @Test
  public void testSerializeDeserialize_shouldReturnSameObject() throws IOException {
    final ObjectMapper om = DummyObjectMapperProvider.get();
    final HighestValue function = HighestValue.of(PRICE_CLOSE, PRICE_OPEN).fromBarsAgo(1).toBarsAgo(3);
    final String serialized = om.writeValueAsString(function);
    assertThat(om.readValue(serialized, Operation.class)).isEqualTo(function);
  }
}

package com.bn.ninjatrader.simulation.operation.function;

import com.bn.ninjatrader.common.util.TestUtil;
import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.History;
import com.bn.ninjatrader.simulation.model.World;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static com.bn.ninjatrader.simulation.operation.Variables.PRICE_CLOSE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Brad on 8/29/16.
 */
public class LowestValueTest {

  private History history;
  private World world;

  private BarData bar1;
  private BarData bar2;
  private BarData bar3;

  @Before
  public void setup() {
    history = mock(History.class);
    world = mock(World.class);

    bar1 = BarData.builder().world(world).addData(PRICE_CLOSE, 4).build();
    bar2 = BarData.builder().world(world).addData(PRICE_CLOSE, 1.00001).build();
    bar3 = BarData.builder().world(world).addData(PRICE_CLOSE, 1.0).build();

    when(world.getHistory()).thenReturn(history);

    when(history.getNBarsAgo(1)).thenReturn(Optional.of(bar1));
    when(history.getNBarsAgo(2)).thenReturn(Optional.of(bar2));
    when(history.getNBarsAgo(3)).thenReturn(Optional.of(bar3));
  }

  @Test
  public void testGetValue_shouldReturnLowestValueAmongNBars() {
    assertThat(LowestValue.of(PRICE_CLOSE, 1).getValue(bar1)).isEqualTo(4.0);
    assertThat(LowestValue.of(PRICE_CLOSE, 2).getValue(bar1)).isEqualTo(1.00001);
    assertThat(LowestValue.of(PRICE_CLOSE, 3).getValue(bar1)).isEqualTo(1.0);
  }

  @Test
  public void testChangeNumOfBarsAgo_shoulrReturnNewObject() {
    final LowestValue function = LowestValue.of(PRICE_CLOSE);
    assertThat(function.inNumOfBarsAgo(3)).isNotEqualTo(function);
    assertThat(function.inNumOfBarsAgo(10)).isEqualTo(LowestValue.of(PRICE_CLOSE, 10));
  }

  @Test
  public void testSerializeDeserialize_shouldReturnSameObject() throws IOException {
    final ObjectMapper om = TestUtil.objectMapper();
    final LowestValue function = new LowestValue(PRICE_CLOSE, 1);
    final String serialized = om.writeValueAsString(function);
    assertThat(om.readValue(serialized, Operation.class)).isEqualTo(function);
  }
}

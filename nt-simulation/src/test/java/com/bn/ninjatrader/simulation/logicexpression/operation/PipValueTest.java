package com.bn.ninjatrader.simulation.logicexpression.operation;

import com.bn.ninjatrader.common.boardlot.BoardLot;
import com.bn.ninjatrader.common.boardlot.BoardLotTable;
import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.World;
import com.bn.ninjatrader.simulation.util.DummyObjectMapperProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class PipValueTest {

  private BarData barData;
  private World world;
  private BoardLotTable boardLotTable;
  private BoardLot boardLot;
  private Price price;

  @Before
  public void before() {
    barData = mock(BarData.class);
    world = mock(World.class);
    boardLotTable = mock(BoardLotTable.class);
    boardLot = mock(BoardLot.class);
    price = mock(Price.class);

    when(barData.getWorld()).thenReturn(world);
    when(barData.getPrice()).thenReturn(price);
    when(world.getBoardLotTable()).thenReturn(boardLotTable);
    when(boardLotTable.getBoardLot(anyDouble())).thenReturn(boardLot);
    when(boardLot.getTick()).thenReturn(0.02);
  }

  @Test
  public void testGetValue_shouldReturnTotalPipValue() {
    when(boardLot.getTick()).thenReturn(0.02);

    assertThat(PipValue.of(3).getValue(barData)).isEqualTo(0.06);

    when(boardLot.getTick()).thenReturn(0.0001);

    assertThat(PipValue.of(2).getValue(barData)).isEqualTo(0.0002);
  }

  @Test
  public void testWithZeroOrNegativePips_shouldThrowException() {
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
      PipValue.of(0);
    });

    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
      PipValue.of(-1);
    });
  }

  @Test
  public void testSerializeDeserialize_shouldReturnSameObject() throws IOException {
    final ObjectMapper om = DummyObjectMapperProvider.get();
    final String serialized = om.writeValueAsString(PipValue.of(3));
    assertThat(om.readValue(serialized, Operation.class)).isEqualTo(PipValue.of(3));
  }
}

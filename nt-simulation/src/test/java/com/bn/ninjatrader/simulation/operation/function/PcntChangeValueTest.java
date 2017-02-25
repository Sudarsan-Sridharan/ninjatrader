package com.bn.ninjatrader.simulation.operation.function;

import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.util.DummyObjectMapperProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static com.bn.ninjatrader.simulation.operation.Variables.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class PcntChangeValueTest {

  private final PcntChangeValue orig = PcntChangeValue.of(PRICE_OPEN, PRICE_CLOSE);
  private final PcntChangeValue equal = PcntChangeValue.of(PRICE_OPEN, PRICE_CLOSE).relativeToLeft();
  private final PcntChangeValue diffLhs = PcntChangeValue.of(PRICE_HIGH, PRICE_CLOSE);
  private final PcntChangeValue diffRhs = PcntChangeValue.of(PRICE_OPEN, PRICE_HIGH);
  private final PcntChangeValue diffRelative = PcntChangeValue.of(PRICE_OPEN, PRICE_CLOSE).relativeToRight();

  private BarData barData;

  @Before
  public void before() {
    barData = mock(BarData.class);
  }

  @Test
  public void testValue_shouldReturnPercentChangeValue() {
    when(barData.get(PRICE_OPEN)).thenReturn(1d);
    when(barData.get(PRICE_CLOSE)).thenReturn(2d);

    assertThat(orig.getValue(barData)).isEqualTo(1);
    assertThat(diffRelative.getValue(barData)).isEqualTo(0.5);
  }

  @Test
  public void testEqual_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(equal).isEqualTo(orig);
    assertThat(orig).isEqualTo(orig).isEqualTo(equal)
        .isNotEqualTo(diffLhs)
        .isNotEqualTo(diffRhs)
        .isNotEqualTo(diffRelative);
  }

  @Test
  public void testHashcode_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, equal, diffLhs, diffRhs, diffRelative))
        .containsExactlyInAnyOrder(orig, diffLhs, diffRhs, diffRelative);
  }


  @Test
  public void testSerializeDeserialize_shouldReturnSameObject() throws IOException {
    final ObjectMapper om = DummyObjectMapperProvider.get();
    final String serialized = om.writeValueAsString(orig);
    assertThat(om.readValue(serialized, Operation.class)).isEqualTo(orig);
  }
}

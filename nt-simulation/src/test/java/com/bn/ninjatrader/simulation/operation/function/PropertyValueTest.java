package com.bn.ninjatrader.simulation.operation.function;

import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.simulation.data.BarData;
import com.bn.ninjatrader.simulation.model.LocalProperties;
import com.bn.ninjatrader.simulation.model.Property;
import com.bn.ninjatrader.simulation.model.World;
import com.bn.ninjatrader.simulation.util.DummyObjectMapperProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class PropertyValueTest {

  private static final String CORRECT_KEY = "CORRECT_KEY";
  private static final String WRONG_KEY = "WRONG_KEY";

  private BarData barData;
  private World world;
  private LocalProperties properties;

  @Before
  public void before() {
    barData = mock(BarData.class);
    world = mock(World.class);
    properties = mock(LocalProperties.class);

    when(barData.getWorld()).thenReturn(world);
    when(world.getProperties()).thenReturn(properties);
    when(properties.get(CORRECT_KEY)).thenReturn(Property.of(CORRECT_KEY, 100d));
    when(properties.containsKey(CORRECT_KEY)).thenReturn(Boolean.TRUE);
  }

  @Test
  public void testGetValue_shouldReturnPropertyValue() {
    assertThat(PropertyValue.of(CORRECT_KEY).getValue(barData)).isEqualTo(100d);
    assertThat(PropertyValue.of(WRONG_KEY).getValue(barData)).isEqualTo(Double.NaN);
  }

  @Test
  public void testSerializeDeserialize_shouldReturnSameObject() throws IOException {
    final ObjectMapper om = DummyObjectMapperProvider.get();
    final PropertyValue function = PropertyValue.of(CORRECT_KEY);
    final String serialized = om.writeValueAsString(function);
    assertThat(om.readValue(serialized, Operation.class)).isEqualTo(function);
  }
}
